#include "handler.hpp"
#include "lib.hpp"
#include "rover.hpp"

class RoverController : public Handler
{
private:
    Rover rover;
public:
    RoverController();
    void handle(char *data);
    int getDataSize();
};