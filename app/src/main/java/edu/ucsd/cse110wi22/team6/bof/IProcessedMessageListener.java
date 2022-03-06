package edu.ucsd.cse110wi22.team6.bof;

import java.util.UUID;

// Listener for "processed" messages (not raw message bytes
public interface IProcessedMessageListener {
    // Advertise the presence of someone
    void onAdvertise(IPerson person);
    // A wave message from `from` to `to`
    void onWave(IPerson from, UUID to);
}
