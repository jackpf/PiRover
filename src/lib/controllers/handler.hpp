#ifndef H_HANDLER
#define H_HANDLER

class Handler
{
public:
    virtual void handle(char *data, int len) = 0;
    virtual int getDataSize() = 0;
};

#endif