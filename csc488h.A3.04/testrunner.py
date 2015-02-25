from enum import Enum
import os
import subprocess
import sys
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
Run the given test and return a TestResult.
'''
def run_test(test_path, compiler_path, should_pass):
    # Run parser on test
    args = ["java", "-jar", compiler_path, "-X", test_path]
    raw_output = subprocess.check_output(args, stderr=subprocess.STDOUT)
    parser_output = raw_output.decode("utf-8")

    # Check if this is a positive or negative test case
    sem_success = semantic_analysis_succeeded(parser_output)
    sem_error = semantic_error_occurred(parser_output)
    test_success = sem_success if should_pass else (not sem_success and sem_error)

    # Return result
    if not parsing_succeeded(parser_output):
        details = str.join("\n", parser_output.split('\n')[2:-2])
        return TestResult(test_path, ResultType.test_fail, details)
    elif not test_success:
        details = str.join("\n", parser_output.split('\n')[1:-2])
        return TestResult(test_path, ResultType.failed, details)
    else:
        return TestResult(test_path, ResultType.passed)


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
Run all tests in the directory at the given path and print the results.
'''
def run_tests(test_dir_path, compiler_path, should_pass):
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
        results.append(result)
    if len(tests):
        print("\n")

    # Print detailed error messages
    errors = [r for r in results if r.type != ResultType.passed]
    for err_result in errors:
        if should_pass:
            print ("  \033[91m" + err_result.test_name + "\033[0m:")
            indented = "    " + str.join("\n    ", err_result.details.split('\n'))
            print(indented + "\n")
        else:
            print ("  \033[91m" + err_result.test_name + "\033[0m")
    if len(errors) and not should_pass:
        print()

    # Recursively run tests in subdirectories
    for subdir in subdirs:
        results += run_tests(subdir, compiler_path, should_pass)

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
    compiler_path = sys.argv[1] if len(sys.argv) > 1 else "./dist/compiler488.jar"
    results = run_tests("./tests/passing/", compiler_path, True)
    results += run_tests("./tests/failing/", compiler_path, False)
    run_time = int(1000 * (timer() - start))

    # Print results
    print(get_result_str(results, run_time))
