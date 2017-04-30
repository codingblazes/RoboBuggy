package com.roboclub.robobuggy.simulation;

import Jama.Matrix;
import com.roboclub.robobuggy.messages.DriveControlMessage;
import com.roboclub.robobuggy.messages.GPSPoseMessage;
import com.roboclub.robobuggy.nodes.baseNodes.BuggyBaseNode;
import com.roboclub.robobuggy.nodes.baseNodes.PeriodicNode;
import com.roboclub.robobuggy.nodes.localizers.LocTuple;
import com.roboclub.robobuggy.nodes.localizers.LocalizerUtil;
import com.roboclub.robobuggy.nodes.localizers.UTMTuple;
import com.roboclub.robobuggy.ros.NodeChannel;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.Subscriber;

import java.util.Date;

/**
 * Created by vivaanbahl on 1/25/17.
 *
 * adapted from @babraham's MATLAB script found in offline/controller
 */
public class ControllerTester extends PeriodicNode {

    // run every 10 ms
    private static final int SIM_PERIOD = 10;
    // controller runs every 10 iterations
    private static final int CONTROLLER_PERIOD = 1;
    // wheelbase in meters
    private static final double WHEELBASE = 1.13;
    // assume a velocity of 8 m/s
    private static final double VELOCITY = 1;
    // assume we are facing up hill 1
    private static final double INITIAL_HEADING_RAD = Math.toRadians(250);

    private Matrix X;
    private Matrix A;
    private double commandedSteeringAngle = 0;
    private Publisher simulatedPosePub;
    private Publisher gpspub;
    private int simCounter;
    private double targetHeading = INITIAL_HEADING_RAD;

    /**
     * Create a new {@link PeriodicNode} decorator
     *
     * @param name
     */
    public ControllerTester(String name, LocTuple initialPosition) {
        super(new BuggyBaseNode(NodeChannel.AUTO), SIM_PERIOD, name);

        LocTuple firstPosition = initialPosition;
        double[][] XAsDoubleArr = {
                { LocalizerUtil.deg2UTM(firstPosition).getEasting() },
                { LocalizerUtil.deg2UTM(firstPosition).getNorthing() },
                { VELOCITY },
                { INITIAL_HEADING_RAD },
                { 0 }
        };

        X = new Matrix(XAsDoubleArr);

        new Subscriber("controller tester", NodeChannel.DRIVE_CTRL.getMsgPath(), ((topicName, m) -> {
            commandedSteeringAngle = ((DriveControlMessage) m).getAngleDouble();
//            targetHeading = X.get(3, 0) + commandedSteeringAngle;
        }));

        simulatedPosePub = new Publisher(NodeChannel.POSE.getMsgPath());
        gpspub = new Publisher(NodeChannel.GPS.getMsgPath());
    }

    @Override
    protected void update() {
        simCounter++;

        A = getNewModel(X);

        // update simulated position
        X = A.times(X);

        UTMTuple t = new UTMTuple(17, 'T', X.get(0, 0), X.get(1, 0));
        LocTuple lt = LocalizerUtil.utm2Deg(t);

        // if it's time to run the controller, update the controller's understanding of where we are
        if (simCounter == CONTROLLER_PERIOD) {
            simulatedPosePub.publish(new GPSPoseMessage(new Date(), lt.getLatitude(), lt.getLongitude(), X.get(3, 0), X.get(2, 0)));
            simCounter = 0;
        }

    }

    private Matrix getNewModel(Matrix x) {
        double dt = 1.0/SIM_PERIOD;
        double[][] matrixAsDoubleArr = {
                { 1, 0, dt * Math.cos((x.get(3, 0))), 0, 0 },
                { 0, 1, dt * Math.sin((x.get(3, 0))), 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 0, 0, 1, dt },
                { 0, 0, Math.tan(commandedSteeringAngle)/WHEELBASE, 0, 0 }
        };
        return new Matrix(matrixAsDoubleArr);
    }

    @Override
    protected boolean startDecoratorNode() {
        resume();
        return true;
    }

    @Override
    protected boolean shutdownDecoratorNode() {
        return false;
    }
}
