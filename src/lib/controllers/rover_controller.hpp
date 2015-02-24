#include "handler.hpp"
#include "lib.hpp"
#include "rover.hpp"

class RoverController : public Handler
{
private:
    Rover rover;
public:
    RoverController();
    ReturnData handle(char *data);
    void cleanup();
    int getDataSize();
};