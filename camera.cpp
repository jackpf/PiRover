#include <ctime>
#include <iostream>
#include <raspicam/raspicam_cv.h>
using namespace std; 
 
int main ( int argc,char **argv ) {
   
    raspicam::RaspiCam_Cv Camera;
    cv::Mat image(640, 480, CV_8UC1);
    int nCount = 10;
    vector<uchar> buf;
    //Camera.set( CV_CAP_PROP_FORMAT, CV_8UC1 );
    cout<<"Opening Camera..."<<endl;
    if (!Camera.open()) {cerr<<"Error opening the camera"<<endl;return -1;}
    //Start capture
    cout<<"Warming up"<<endl;
    for ( int i=0; i<nCount; i++ ) {
        Camera.grab();
    }

    while (true) {
        Camera.grab();
        Camera.retrieve ( image);
        cout << "Capture" << endl;
        cv::imencode(".jpg",image, buf);
    }
    cout<<"Stop camera..."<<endl;
    Camera.release();
}