#include "gtest/gtest.h"
#include "rover.hpp"

TEST(Controller, FullAccelerationFullRightSteering)
{
    Rover rover;
    Rover::MotorValues values = rover.calculateMotorValues(10, 10);

    EXPECT_EQ(values.left.motorA, 10);
    EXPECT_EQ(values.left.motorB, 0);
    EXPECT_EQ(values.right.motorA, 0);
    EXPECT_EQ(values.right.motorB, 10);
}

TEST(Controller, FullAccelerationFullLeftSteering)
{
    Rover rover;
    Rover::MotorValues values = rover.calculateMotorValues(10, -10);

    EXPECT_EQ(values.left.motorA, 0);
    EXPECT_EQ(values.left.motorB, 10);
    EXPECT_EQ(values.right.motorA, 10);
    EXPECT_EQ(values.right.motorB, 0);
}

TEST(Controller, HalfAccelerationNoSteering)
{
    Rover rover;
    Rover::MotorValues values = rover.calculateMotorValues(5, 0);

    EXPECT_EQ(values.left.motorA, 5);
    EXPECT_EQ(values.left.motorB, 0);
    EXPECT_EQ(values.right.motorA, 5);
    EXPECT_EQ(values.right.motorB, 0);
}

TEST(Controller, HalfAccelerationHalfRightSteering)
{
    Rover rover;
    Rover::MotorValues values = rover.calculateMotorValues(5, 5);

    EXPECT_EQ(values.left.motorA, 5);
    EXPECT_EQ(values.left.motorB, 0);
    EXPECT_EQ(values.right.motorA, 0);
    EXPECT_EQ(values.right.motorB, 0);
}

TEST(Controller, HalfAccelerationFullLeftSteering)
{
    Rover rover;
    Rover::MotorValues values = rover.calculateMotorValues(5, -10);

    EXPECT_EQ(values.left.motorA, 0);
    EXPECT_EQ(values.left.motorB, 5);
    EXPECT_EQ(values.right.motorA, 5);
    EXPECT_EQ(values.right.motorB, 0);
}

TEST(Controller, NoAccelerationOrSteering)
{
    Rover rover;
    Rover::MotorValues values = rover.calculateMotorValues(0, 0);

    EXPECT_EQ(values.left.motorA, 0);
    EXPECT_EQ(values.left.motorB, 0);
    EXPECT_EQ(values.right.motorA, 0);
    EXPECT_EQ(values.right.motorB, 0);
}

TEST(Controller, CannotAccessMotorValues)
{
    Rover rover;
    Rover::MotorValues values = rover.calculateMotorValues(0, 0);

    values.left.motorA = 5;

    values = rover.getMotorValues();

    EXPECT_EQ(values.left.motorA, 0);
    EXPECT_EQ(values.left.motorB, 0);
    EXPECT_EQ(values.right.motorA, 0);
    EXPECT_EQ(values.right.motorB, 0);
}