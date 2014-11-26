#include "lib.hpp"

namespace Lib
{
    struct argp_option *Args::options = NULL;
    int Args::numOptions = 0;
    int Args::argCount = 0;

    Args::Args(int argc, char *argv[], struct argp_option *options, size_t optionsLen)
    {
        this->options = options;
        this->numOptions = optionsLen / sizeof(struct argp_option) - 1 /* -1 for {0} */;

        static struct argp argp = {options, _parse_opt, 0, 0};

        argp_parse(&argp, argc, argv, 0, 0, &arguments);
    }

    const char *Args::get(const char *key)
    {
        // Just returning arguments[key] doesn't seem to work too well...
        ArgumentsIterator it;

        for (it = arguments.begin(); it != arguments.end(); it++) {
            if (strcmp(it->first, key) == 0) {
                return (const char *) it->second;
            }
        }

        return NULL;
    }

    const char *Args::get(int key)
    {
        char num[5];
        snprintf(num, 5, "%d", key);

        return get(num);
    }

    const char *Args::get(const char *key, const char defaultValue[])
    {
        return arguments[key] != NULL ? (const char *) arguments[key] : defaultValue;
    }

    int Args::count()
    {
        return argCount;
    }

    error_t Args::_parse_opt(int key, char *arg, struct argp_state *state)
    {
        Arguments *arguments = (Arguments *) state->input;

        switch (key) {
            case ARGP_KEY_END: {
                for (int i = 0; i < numOptions; i++) {
                    if (options[i].flags & OPTION_REQUIRED && (*arguments)[options[i].name] == NULL) {
                        //argp_usage(state);
                        println(
                            stderr,
                            "%1$s: required option missing -- '%2$c'\nTry `%1$s --help' or `%1$s --usage' for more information.\n",
                            state->argv[0],
                            options[i].key
                        );
                        exit(-1);
                    }
                }
            } break;
            case ARGP_KEY_ARG: {
                char *num = (char *) malloc(5 * sizeof(char));
                sprintf(num, "%d", state->arg_num);
                (*arguments)[num] = arg;
                argCount++;
            } break;
            default: {
                for (int i = 0; i < numOptions; i++) {
                    if (options[i].key == key) {
                        (*arguments)[options[i].name] = arg;
                    }
                }
            } break;
        }

        return 0;
    }

    void println(const char *format, ...)
    {
        va_list args;
        va_start(args, format);
        vprintf(format, args);
        va_end(args);
        printf("\n");
    }

    void println(FILE *file, const char *format, ...)
    {
        va_list args;
        va_start(args, format);
        vfprintf(file, format, args);
        va_end(args);
        printf("\n");
    }
}