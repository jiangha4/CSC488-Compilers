import os;
import re;

'''
Print tests descriptions.
'''
def print_tests(path, indent=0):
    # Get subdirectories and tests in this directory
    dir_items = [d for d in os.listdir(path) if not d.startswith(".")]
    item_paths = [os.path.join(path, d) for d in dir_items]
    subdirs = [d for d in item_paths if not os.path.isfile(d)]
    tests = [d for d in item_paths if not d in subdirs if d.endswith(".488")]

    # Print subdirs recursively
    for subdir in subdirs:
        dir_name = os.path.basename(subdir)
        descr = re.sub("([A-Z])"," \g<0>", dir_name).lower().split(".")[0]
        print("\t" * indent + descr + ":")
        print_tests(subdir, indent + 1)

    # Print tests names
    for test in tests:
        file_name = os.path.basename(test)
        descr = re.sub("([A-Z])"," \g<0>", file_name).lower().split(".")[0]
        print("\t" * indent + descr)
    print()

'''
Main.
'''
if __name__ == '__main__':
    print_tests('./tests/')
