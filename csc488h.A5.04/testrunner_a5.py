from enum import Enum
import os
import subprocess
import sys
import shutil
from timeit import default_timer as timer


'''
Enum describing the possible test result types.
'''
class ResultType(Enum):
    passed = 1
    failed = 2
    test_fail = 3


'''
Class describing the test result type as well as optionally a detail string.
'''
class TestResult:
    def __init__(self, test, result_type, detail=None):
        self.test_name = test
        self.type = result_type
        self.details = detail


'''
Return True iff the parser succesfully parsed the input.
'''
def parsing_succeeded(parser_output):
    return "Syntax error" not in parser_output


'''
Return True iff the semantic analysis was successful.
'''
def semantic_analysis_succeeded(parser_output):
    return "Exception during Semantic Analysis" not in parser_output


'''
Return True iff there was a semantic error.
'''
def semantic_error_occurred(parser_output):
    return "SemanticErrorException" in parser_output


'''
Return True iff code generation was successful.
'''
def code_generation_succeeded(parser_output):
    return "Exception during Code Generation" not in parser_output


'''
Return True iff there was a runtime error.
'''
def runtime_error_occurred(parser_output):
    return "Exception during Machine Execution" in parser_output



'''
Run the given test and return a TestResult.
'''
def run_test(test_path, compiler_path, should_pass):
    # Run parser on test
    args = ["java", "-jar", compiler_path, "-T", "c", test_path]
    raw_output = subprocess.check_output(args, stderr=subprocess.STDOUT)
    parser_output = raw_output.decode("utf-8")

    # Check if this is a positive or negative test case
    gen_success = code_generation_succeeded(parser_output)
    run_error = runtime_error_occurred(parser_output)
    test_success = (not run_error) if should_pass else run_error

    # Return result
    if not parsing_succeeded(parser_output):
        details = str.join("\n", parser_output.split('\n')[2:-2])
        return TestResult(test_path, ResultType.test_fail, details)
    elif not semantic_analysis_succeeded(parser_output):
        if semantic_error_occurred(parser_output):
            split = parser_output.split('\n')
            details = str.join("\n", [split[4], split[-3]])
        else:
            details = str.join("\n", parser_output.split('\n')[3:-2])
        return TestResult(test_path, ResultType.test_fail, details)
    elif not code_generation_succeeded(parser_output):
        details = str.join("\n", parser_output.split('\n')[3:-2])
        return TestResult(test_path, ResultType.failed, details)
    elif not test_success:
        file_name = os.path.join('./test_logs', test_path[2:-4].replace('/', '_'))
        file = open(file_name, 'w+')
        details = str.join("\n", parser_output.split('\n')[3:-2])
        file.write(details)
        file.close()
        msg = "Runtime error, see '" + file_name + "'"
        return TestResult(test_path, ResultType.failed, msg)
    else:
        details = str.join("\n", parser_output.split('\n')[:])
        return TestResult(test_path, ResultType.passed, details)


'''
Prints the result of running a single test.
'''
def print_test_result(result):
    if result.type == ResultType.passed:
        print("\033[92m.\033[0m", end="")
    elif result.type == ResultType.failed:
        print("\033[91mF\033[0m", end="")
    else:
        print("\033[91mF\033[0m", end="")
    sys.stdout.flush()


'''
Write the standard output from the test to the given file.
'''
def write_output_to_file(test_path, file, result):
    file.write(test_path + ":")

    start = result.details.find("Start Execution  pc")
    end = result.details.find("End Execution")
    output_lines = result.details[start:end].split('\n')[2:-1]
    file.write('\n\t' + str.join("\n\t", output_lines))
    file.write('\n\n')


'''
Run all tests in the directory at the given path and print the results.
'''
def run_tests(test_dir_path, compiler_path, test_output_file, should_pass):
    # Get subdirectories and tests in this directory
    dir_items = [d for d in os.listdir(test_dir_path) if not d.startswith(".")]
    item_paths = [os.path.join(test_dir_path, d) for d in dir_items]
    subdirs = [d for d in item_paths if not os.path.isfile(d)]
    tests = [d for d in item_paths if not d in subdirs if d.endswith(".488")]

    # Run tests
    results = []
    if len(tests):
        # Print directory path
        print("\033[94m" + test_dir_path + "\033[0m:\n  ", end="")
    for test_path in tests:
        # Run parser on test, print result
        result = run_test(test_path, compiler_path, should_pass)
        print_test_result(result)
        if should_pass:
            write_output_to_file(test_path, test_output_file, result)
        results.append(result)
    if len(tests):
        print("\n")

    # Print detailed error messages
    errors = [r for r in results if r.type != ResultType.passed]
    add_newline = False
    for err_result in errors:
        if err_result.details:
            print ("  \033[91m" + err_result.test_name + "\033[0m:")
            indented = "    " + str.join("\n    ", err_result.details.split('\n'))
            print(indented + "\n")
            add_newline = False
        else:
            print ("  \033[91m" + err_result.test_name + "\033[0m")
            add_newline = True
    if add_newline:
        print()

    # Recursively run tests in subdirectories
    for subdir in subdirs:
        results += run_tests(subdir, compiler_path, test_output_file, should_pass)

    return results


'''
Return a string summarizing the test results.
'''
def get_result_str(results, run_time):
    # Parse results
    num_pass = len([r for r in results if r.type == ResultType.passed])
    num_fail = len([r for r in results if r.type == ResultType.failed])
    num_test_fail = len([r for r in results if r.type == ResultType.test_fail])

    # Construct time and pass/fail summaries
    time_str = "Ran " + str(len(results)) + " tests in " + str(run_time) + "ms."
    pass_str = str(num_pass) + " passed" if num_pass else ""
    fail_str = str(num_fail) + " failed" if num_fail else ""
    test_fail_str = str(num_test_fail) + " invalid" if num_test_fail else ""
    result_strs = [s for s in [pass_str, fail_str, test_fail_str] if s != ""]
    result_str = str.join(", ", result_strs)

    # Construct and return total string
    total_str = "\033[91m" if num_fail or num_test_fail else "\033[92m"
    total_str += time_str + " " + result_str
    total_str += ".\033[0m"

    return total_str


'''
Main.
'''
if __name__ == '__main__':
    # Run tests, measure performance
    start = timer()
    shutil.rmtree("./test_logs", True)
    os.mkdir("./test_logs")
    test_output_file = open("./test_logs/OUTPUT", 'w+')
    compiler_path = sys.argv[1] if len(sys.argv) > 1 else "./dist/compiler488.jar"
    results = run_tests("./testing/pass/", compiler_path, test_output_file, True)
    results += run_tests("./testing/fail/", compiler_path, test_output_file, False)
    run_time = int(1000 * (timer() - start))

    # Print results
    print(get_result_str(results, run_time))
