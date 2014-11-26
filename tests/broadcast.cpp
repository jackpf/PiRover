#include "gtest/gtest.h"
#include <pthread.h>
#include "broadcast.hpp"

#define PORT 2080

pthread_t listener_thread;

void *spawn_listener(void *data)
{
    Broadcast broadcaster(PORT);

    broadcaster.listen("PiRover", "return string");

    return NULL;
}

void create_listener_thread()
{
    pthread_create(&listener_thread, NULL, spawn_listener, NULL);
    sleep(1); // Give listener time to boot up
}

void destroy_listener_thread()
{
    pthread_cancel(listener_thread);
}

TEST(Broadcast, ResolveIP)
{
    create_listener_thread();

    Broadcast broadcaster(PORT);

    EXPECT_STRNE(broadcaster.resolve("PiRover"), NULL);
    sleep(1);
    EXPECT_STREQ(broadcaster.resolve("this is wrong :("), NULL);

    destroy_listener_thread();
}

TEST(Broadcast, ResolveIPNoListener)
{
    Broadcast broadcaster(PORT);

    EXPECT_STREQ(broadcaster.resolve("PiRover"), NULL);
}