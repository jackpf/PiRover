package com.jackpf.pirover.test;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.jackpf.pirover.MainActivity;
import com.jackpf.pirover.Broadcast.BroadcastResolver;
import com.jackpf.pirover.Broadcast.ConnectionException;
import com.jackpf.pirover.Broadcast.TimeoutException;
import com.jackpf.pirover.Model.Request;
import com.jackpf.pirover.Model.RequestResponse;
import com.jackpf.pirover.Request.BroadcastRequest;

public class BroadcastTest extends ActivityInstrumentationTestCase2<MainActivity>
{
    private Context context;
    
    public BroadcastTest()
    {
        super(MainActivity.class);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

        context = getInstrumentation().getContext();
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    
    private RequestResponse sendRequest() throws Exception
    {
        Request request = new BroadcastRequest(
            context.getSystemService(Context.WIFI_SERVICE),
            context.getSystemService(Context.CONNECTIVITY_SERVICE)
        );
        
        try {
            return request.call();
        } catch (ConnectionException e) {
            fail("Connection failed: " + e.getMessage());
        } catch (TimeoutException e) {
            fail("Timeout: " + e.getMessage());
        }
        
        return null;
    }
    
    public void testResolveIp() throws Exception
    {
        RequestResponse response = sendRequest();
        
        assertNotNull(response.get("ip"));
    }
    
    public void testResolvePorts() throws Exception
    {
        RequestResponse response = sendRequest();
        
        BroadcastResolver.PortMap ports = (BroadcastResolver.PortMap) response.get("ports");
        
        assertNotNull(ports);
        assertNotNull(ports.get("control"));
        assertNotNull(ports.get("camera"));
    }
}
