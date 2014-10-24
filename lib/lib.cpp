#include "lib.hpp"

namespace Lib
{
    struct argp_option *Args::options = NULL;
    int Args::numOptions = 0;

    void Args::parse(int argc, char *argv[], struct argp_option *options, size_t optionsLen)
    {
        this->options = options;
        this->numOptions = optionsLen / sizeof(struct argp_option) - 1 /* -1 for {0} */;

        static struct argp argp = {options, _parse_opt, 0, 0};

        argp_parse(&argp, argc, argv, 0, 0, &arguments);
    }

    const char *Args::get(const char *key)
    {
        return (const char *) arguments[key];
    }

    const char *Args::get(const char *key, const char defaultValue[])
    {
        return arguments[key] != NULL ? (const char *) arguments[key] : defaultValue;
    }

    error_t Args::_parse_opt(int key, char *arg, struct argp_state *state)
    {
        Arguments *arguments = (Arguments *) state->input;

        if (key == ARGP_KEY_ARG) {
            return 0;
        } else if (key == ARGP_KEY_END) {
            for (int i = 0; i < numOptions; i++) {
                if (options[i].flags & OPTION_REQUIRED && (*arguments)[options[i].name] == NULL) {
                    //argp_usage(state);
                    printf(
                        "%1$s: required option missing -- '%2$c'\nTry `%1$s --help' or `%1$s --usage' for more information.\n",
                        state->argv[0],
                        options[i].key
                    );
                    exit(-1);
                }
            }
        }

        if (key == 0) { // Normal argument
            char num[5];
            sprintf(num, "%d", state->arg_num);
            (*arguments)[(const char *) num] = arg;
        } else { // Option
            for (int i = 0; i < numOptions; i++) {
                if (options[i].key == key) {
                    (*arguments)[options[i].name] = arg;
                }
            }
        }

        return 0;
    }
}