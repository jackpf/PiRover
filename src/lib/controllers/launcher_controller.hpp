#include "handler.hpp"
#include "lib.hpp"
#include "launcher.hpp"

class LauncherController : public Handler
{
private:
    Launcher launcher;
public:
    LauncherController();
    void handle(void *data);
    int getDataSize();
};