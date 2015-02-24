#include "handler.hpp"
#include "lib.hpp"
#include "launcher.hpp"

class LauncherController : public Handler
{
private:
    Launcher launcher;
public:
    LauncherController();
    ReturnData handle(char *data);
    void cleanup();
    int getDataSize();
};