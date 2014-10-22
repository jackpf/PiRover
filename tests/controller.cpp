#include "gtest/gtest.h"

#include "../lib/rover.hpp"

TEST(Controller, NoAccelerationOrSteering)
{
    Rover rover;
    MotorValues values = rover.process(0, 0);

    EXPECT_EQ(values.leftA, 0);
    EXPECT_EQ(values.leftB, 0);
    EXPECT_EQ(values.rightA, 0);
    EXPECT_EQ(values.rightB, 0);
}

TEST(Controller, FullAccelerationFullRightSteering)
{
    Rover rover;
    MotorValues values = rover.process(10, 10);

    EXPECT_EQ(values.leftA, 10);
    EXPECT_EQ(values.leftB, 0);
    EXPECT_EQ(values.rightA, 0);
    EXPECT_EQ(values.rightB, 10);
}

TEST(Controller, FullAccelerationFullLeftSteering)
{
    Rover rover;
    MotorValues values = rover.process(10, -10);

    EXPECT_EQ(values.leftA, 0);
    EXPECT_EQ(values.leftB, 10);
    EXPECT_EQ(values.rightA, 10);
    EXPECT_EQ(values.rightB, 0);
}

TEST(Controller, HalfAccelerationNoSteering)
{
    Rover rover;
    MotorValues values = rover.process(5, 0);

    EXPECT_EQ(values.leftA, 5);
    EXPECT_EQ(values.leftB, 0);
    EXPECT_EQ(values.rightA, 5);
    EXPECT_EQ(values.rightB, 0);
}

TEST(Controller, HalfAccelerationHalfRightSteering)
{
    Rover rover;
    MotorValues values = rover.process(5, 5);

    EXPECT_EQ(values.leftA, 5);
    EXPECT_EQ(values.leftB, 0);
    EXPECT_EQ(values.rightA, 0);
    EXPECT_EQ(values.rightB, 0);
}

TEST(Controller, HalfAccelerationFullLeftSteering)
{
    Rover rover;
    MotorValues values = rover.process(5, -10);

    EXPECT_EQ(values.leftA, 0);
    EXPECT_EQ(values.leftB, 5);
    EXPECT_EQ(values.rightA, 5);
    EXPECT_EQ(values.rightB, 0);
}