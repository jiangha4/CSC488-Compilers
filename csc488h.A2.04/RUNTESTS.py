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
Run all tests in the directory at the given path and print the results.
'''
def run_tests(path):
    # Get subdirectories and tests in this directory
    dir_items = [d for d in os.listdir(path) if not d.startswith(".")]
    item_paths = [os.path.join(path, d) for d in dir_items]
    subdirs = [d for d in item_paths if not os.path.isfile(d)]
    tests = [d for d in item_paths if not d in subdirs if d.endswith(".488")]

    # Run tests
    errors = []
    if len(tests):
        # Print path
        print("" + path + ":\n  ", end="")
    for test_path in tests:
        # Run parser on test
        args = ["java", "-jar", COMPILER_BIN, test_path]
        raw_output = subprocess.check_output(args, stderr=subprocess.STDOUT)
        parser_output = raw_output.decode("utf-8")

        # Check and print result
        if "Exception during Parsing" in parser_output:
            errors.append((test_path, parser_output))
            print("\033[91mF\033[0m", end="")
        else:
            print("\033[92m.\033[0m", end="")
    print("\n")

    # Print detailed error messages
    for test_path, err_detail in errors:
        print ("  \033[91m" + test_path + "\033[0m:")
        indented = "    " + str.join("\n    ", err_detail.split(sep='\n')[1:-2])
        print(indented + "\n")

    # Recursively run tests in subdirectories
    num_tests = len(tests)
    num_errors = len(errors)
    for subdir in subdirs:
        sub_tests, sub_erros = run_tests(subdir)
        num_tests += sub_tests
        num_errors += sub_erros

    return num_tests, num_errors


'''
Main.
'''
if __name__ == '__main__':
    # Run tests, measure performance
    start = timer()
    num_tests, num_errs = run_tests(TEST_DIR)
    run_time = int(1000 * (timer() - start))

    # Print results
    print("\033[91m" if num_errs else "\033[92m", end="")
    line1 = "Ran " + str(num_tests) + " tests in " + str(run_time) + "ms."
    line2 = str(num_tests - num_errs) + " passed, " + str(num_errs) + " failed."
    print(line1 + " " + line2 + "\033[0m")
