#include <argp.h>
#include <map>
#include <stdlib.h>

/**
 * Extra flag to specify that option is required
 */
#define OPTION_REQUIRED 512

namespace Lib
{
    class Args
    {
    public:

        /**
        * Argument list type
        */
        typedef std::map<const char *, char *> Arguments;

    private:

        /**
         * Arguments map
         */
        Arguments arguments;

        /**
         * Options to parse
         */
        static struct argp_option *options;

        /**
         * Number of options
         */
        static int numOptions;

    public:

        /**
         * Parse arguments
         *
         * @param argc          Arg count
         * @param argv          Arg values
         * @param options       Options to parse
         * @param optionsLen    Length of options
         */
        void parse(int argc, char *argv[], struct argp_option *options, size_t optionsLen);

        /**
         * Get option by name
         * Regular arguments are accessed by the position they appear
         *
         * @param key           Name of argument
         * @return              Argument value
         */
        const char *get(const char *key);

        /**
         * Get option by name and return a default value if it is not set
         *
         * @param key           Name of argument
         * @param defaultValue  Value to return if arg is not set
         * @return              Argument value
         */
        const char *get(const char *key, const char defaultValue[]);

        /**
         * Internal options parser
         */
        static error_t _parse_opt(int key, char *arg, struct argp_state *state);
    };
}