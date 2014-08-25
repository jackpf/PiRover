#include "lib.hpp"

void assert_args(int argc, int assert)
{
    if (argc < assert + 1) {
        printf("Expected %d args but got %d\n", assert, argc - 1);
        exit(-1);
    }
}