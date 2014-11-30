#include <argp.h>
#include <map>
#include <stdlib.h>
#include <string.h>
#include <stdarg.h>
#include <sys/time.h>

#ifndef LIB_H
#define LIB_H

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
         * Arguments map type
         */
        typedef std::map<const char *, char *> Arguments;

        /**
         * Argument options type
         */
        typedef struct argp_option ArgumentOptions;

        /**
         * Arguments iterator type
         */
        typedef std::map<const char*, char*>::iterator ArgumentsIterator;

    private:

        /**
         * Arguments map
         */
        Arguments arguments;

        /**
         * Options to parse
         */
        static ArgumentOptions *options;

        /**
         * Number of options
         */
        static int numOptions;

        /**
         * Number of arguments, excluding options
         */
        static int argCount;

    public:

        /**
         * Constructor
         *
         * @param argc          Arg count
         * @param argv          Arg values
         * @param options       Options to parse
         * @param optionsLen    Length of options
         */
        Args(int argc, char *argv[], ArgumentOptions *options, size_t optionsLen);

        /**
         * Internal options parser
         */
        static error_t _parse_opt(int key, char *arg, struct argp_state *state);

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
         * Get argument
         *
         * @param key           Position of argument
         * @return              Argument value
         */
        const char *get(int key);

        /**
         * Get arg count
         */
        int count();
    };

    /**
     * Print line
     * Essentially printf() + \n
     */
    void println(const char *format, ...);
    void println(FILE *file, const char *format, ...);

    /**
     * Get timestamp in microseconds
     */
    long getTimestamp();
}

#endif