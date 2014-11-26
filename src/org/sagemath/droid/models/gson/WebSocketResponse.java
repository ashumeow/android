package org.sagemath.droid.models.gson;

/**
 * The Response containing the paths for the websockets.
 *
 * @author Nikhil Peter Raj
 */
public class WebSocketResponse extends BaseResponse {

    private String id;
    private String ws_url;

    public String getKernelID() {
        return id;
    }

    public String getWebSocketURL() {
        return ws_url;
    }

    public boolean isValidResponse() {
        //Some ugly sanity checking, for flavor
        if ((id != null || id != "") && (ws_url != null || ws_url != "")) {
            return true;
        }
        return false;
    }

}
