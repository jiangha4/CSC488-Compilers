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
Run the given test and return a TestResult.
'''
def run_test(test_path, compiler_path, should_pass):
    # Run parser on test
    args = ["java", "-jar", compiler_path, test_path]
    raw_output = subprocess.check_output(args, stderr=subprocess.STDOUT)
    parser_output = raw_output.decode("utf-8")

    # Check if this is a positive or negative test case
    parse_success = parsing_succeeded(parser_output)
    test_success = parse_success if should_pass else not parse_success

    # Return result
    if test_success:
        return TestResult(test_path, ResultType.passed)
    else:
        return TestResult(test_path, ResultType.failed, parser_output)


'''
Prints the result of running a single test.
'''
def print_test_result(result):
    if result.type == ResultType.passed:
        print("\033[92m.\033[0m", end="")
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
    errors = [r for r in results if r.type == ResultType.failed]
    for err_result in errors:
        if should_pass:
            print ("  \033[91m" + err_result.test_name + "\033[0m:")
            indented = "    " + str.join("\n    ", err_result.details.split(sep='\n')[1:-2])
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

    # Construct time and pass/fail summaries
    time_str = "Ran " + str(len(results)) + " tests in " + str(run_time) + "ms."
    pass_str = str(num_pass) + " passed" if num_pass else ""
    fail_str = str(num_fail) + " failed" if num_fail else ""
    result_strs = [s for s in [pass_str, fail_str] if s != ""]
    result_str = str.join(", ", result_strs)

    # Construct and return total string
    total_str = "\033[91m" if num_fail else "\033[92m"
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
