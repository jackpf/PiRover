#include "handler.hpp"
#include "lib.hpp"

class ServoController : public Handler
{
public:
    ServoController();
    ReturnData handle(char *data);
    void cleanup();
    int getDataSize();
};