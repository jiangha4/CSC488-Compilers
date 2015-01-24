from enum import Enum
import os
import subprocess
import sys
from timeit import default_timer as timer


'''
Configuration.
'''
COMPILER_BIN = "./dist/compiler488.jar"
TEST_DIR = "./test"


'''
Enum describing the possible test result types.
'''
class ResultType(Enum):
    passed = 1
    failed = 2
    did_not_run = 3


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
    return "Exception during Parsing" not in parser_output


'''
Run the given test and return a TestResult.
'''
def run_test(path):
    # Check if this test has a valid name
    is_pass_test = path.endswith(".pos.488")
    is_fail_test = path.endswith(".neg.488")
    if not (is_pass_test or is_fail_test):
        return TestResult(path, ResultType.did_not_run)

    # Run parser on test
    args = ["java", "-jar", COMPILER_BIN, path]
    raw_output = subprocess.check_output(args, stderr=subprocess.STDOUT)
    parser_output = raw_output.decode("utf-8")

    # Check if this is a positive or negative test case
    parse_success = parsing_succeeded(parser_output)
    test_success = parse_success if is_pass_test else not parse_success

    # Return result
    if test_success:
        return TestResult(path, ResultType.passed)
    else:
        return TestResult(path, ResultType.failed, parser_output)


'''
Prints the result of running a single test.
'''
def print_test_result(result):
    if result.type == ResultType.passed:
        print("\033[92m.\033[0m", end="")
    elif result.type == ResultType.failed:
        print("\033[91mF\033[0m", end="")
    else:
        print("\033[93m?\033[0m", end="")


'''
Run all tests in the directory at the given path and print the results.
'''
def run_tests(path):
    # Get subdirectories and tests in this directory
    dir_items = [d for d in os.listdir(path) if not d.startswith(".")]
    item_paths = [os.path.join(path, d) for d in dir_items]
    subdirs = [d for d in item_paths if not os.path.isfile(d)]
    tests = [d for d in item_paths if not d in subdirs if d.endswith(".488")]

    # Run tests
    results = []
    if len(tests):
        # Print directory path
        print("\033[94m" + path + "\033[0m:\n  ", end="")
    for test_path in tests:
        # Run parser on test, print result
        result = run_test(test_path)
        print_test_result(result)
        results.append(result)
    if len(tests):
        print("\n")

    # Print detailed error messages
    errors = [r for r in results if r.type == ResultType.failed]
    for err_result in errors:
        print ("  \033[91m" + err_result.test_name + "\033[0m:")
        indented = "    " + str.join("\n    ", err_result.details.split(sep='\n')[1:-2])
        print(indented + "\n")

    # Print invalid tests
    did_not_runs = [r for r in results if r.type == ResultType.did_not_run]
    if did_not_runs:
        print ("  \033[93mDid not run due to invalid name\033[0m:")
        for dnr_name in [r.test_name for r in did_not_runs]:
            print ("    " + dnr_name)
        print("")

    # Recursively run tests in subdirectories
    for subdir in subdirs:
        results += run_tests(subdir)

    return results


'''
Main.
'''
if __name__ == '__main__':
    # Run tests, measure performance
    start = timer()
    results = run_tests(TEST_DIR)
    run_time = int(1000 * (timer() - start))

    # Print results
    num_pass = len([r for r in results if r.type == ResultType.passed])
    num_fail = len([r for r in results if r.type == ResultType.failed])
    num_dnr = len([r for r in results if r.type == ResultType.did_not_run])

    print("\033[91m" if num_fail else "\033[93m" if num_dnr else "\033[92m", end="")
    run_time = "Ran " + str(len(results)) + " tests in " + str(run_time) + "ms."
    pass_str = str(num_pass) + " passed" if num_pass else ""
    fail_str = str(num_fail) + " failed" if num_fail else ""
    dnr_str = str(num_dnr) + " didn't run" if num_dnr else ""
    result_strs = [s for s in [pass_str, fail_str, dnr_str] if s != ""]
    print(run_time + " " + str.join(", ", result_strs) + ".\033[0m")
