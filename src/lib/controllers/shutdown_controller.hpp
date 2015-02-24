#include "handler.hpp"
#include "lib.hpp"

class ShutdownController : public Handler
{
public:
    ReturnData handle(char *data);
    void cleanup();
    int getDataSize();
};