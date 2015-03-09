#ifndef H_HANDLER
#define H_HANDLER

class Handler
{
public:
    typedef struct {
        int len;
        void *data;
    } ReturnData;

    ReturnData NO_DATA;

    virtual ReturnData handle(char *data) = 0;
    virtual void cleanup() = 0;
    virtual int getDataSize() = 0;
};

#endif