package tools.AudioTools;

import java.io.*;
import java.lang.reflect.Method;

import org.robovm.apple.corefoundation.OSStatusException;
import org.robovm.rt.bro.annotation.Callback;
import org.robovm.rt.bro.annotation.Pointer;
import org.robovm.apple.audiotoolbox.*;
import org.robovm.apple.audiotoolbox.AudioQueueBuffer.AudioQueueBufferPtr;
import org.robovm.apple.coreaudio.*;
import org.robovm.apple.coreaudio.AudioTimeStamp.*;
import org.robovm.apple.coreaudio.AudioStreamPacketDescription.AudioStreamPacketDescriptionPtr;
import org.robovm.apple.audiotoolbox.AudioQueue.*;
import org.robovm.apple.corefoundation.CFRunLoopMode;
import org.robovm.apple.coreaudio.AudioErrorCode;
import org.robovm.rt.VM;
import org.robovm.rt.bro.*;
import org.robovm.rt.bro.ptr.*;

public class AudioRecorder {
  protected double mSampleRate;
  protected AudioFormat mFormatID;
  protected int mFormatFlags;
  protected int mBytesPerPacket;
  protected int mFramesPerPacket;
  protected int mBytesPerFrame;
  protected int mChannelsPerFrame;
  protected int mBitsPerChannel;

  protected AudioQueue mQueue = null;

  private int kNumberBuffers = 3;
  private PipedInputStream mPIS;
  private PipedOutputStream mPOS;
  private int mStateID = -1;

  private boolean mRunning = false;

  public AudioRecorder() throws IOException
  {
    mSampleRate = 44100;
    mFormatID = AudioFormat.LinearPCM;
    mFormatFlags = (int)AudioFormatFlags.AudioFormatFlagIsPacked.value() | (int)AudioFormatFlags.AudioFormatFlagIsSignedInteger.value();
    mBytesPerPacket = 2;
    mFramesPerPacket = 1;
    mBytesPerFrame = 2;
    mChannelsPerFrame = 1;
    mBitsPerChannel = 16;

    mPOS = new PipedOutputStream();
    mPIS = new PipedInputStream(mPOS);
  }

  public static int getMinBufferSize(int sampleRate, int channelConfig, int audioFormat)
  {
    return 0;
  }

  public int deriveBufferSize(AudioQueue audioQueue, AudioStreamBasicDescription ASBDescription, double seconds)
  {
    int maxBufferSize = 0x50000;
    int maxPacketSize = ASBDescription.getBytesPerPacket();
    System.out.println(3);
    double numBytesForTime = ASBDescription.getSampleRate() * maxPacketSize * seconds;
    return (int)(numBytesForTime < maxBufferSize ? numBytesForTime : maxBufferSize);
  }

  public void release() throws OSStatusException
  {
    System.out.println("RECORD QUEUE STOPPING...");
    mRunning = false;
    mQueue.stop(true);
//      mQueue.dispose(true);
    System.out.println("RECORD QUEUE STOPPED");
    try
    {
      mPOS.close();
      mPIS.close();
      AQRecorderState.drop(mStateID);
    }
    catch (Exception x) { x.printStackTrace(); }
  }

  public int read(byte[] abData, int i, int length) throws IOException
  {
    return mPIS.read(abData, i, length);
  }

  /*<bind>*/static { Bro.bind(AudioRecorder.class); }/*</bind>*/
  /*<constants>*//*</constants>*/
    /*<constructors>*//*</constructors>*/
    /*<properties>*//*</properties>*/
    /*<members>*//*</members>*/
  @Callback
  public static void callbackMethod(
          @Pointer long                     refcon,
          AudioQueue                        inAQ,
          AudioQueueBuffer                  inBuffer,
          AudioTimeStampPtr                 inStartTime,
          int                               inNumPackets,
          AudioStreamPacketDescriptionPtr   inPacketDesc
  ) throws OSStatusException
  {
    try
    {
      AQRecorderState.AQRecorderStatePtr ptr = new AQRecorderState.AQRecorderStatePtr();
      ptr.set(refcon);
      AQRecorderState aqrs = ptr.get();
      byte[] ba = VM.newByteArray(inBuffer.getDataPointer(), inBuffer.getAudioDataBytesCapacity());
      aqrs.getRecord().receive(ba);
    }
    catch (Exception x) { x.printStackTrace(); }

    inAQ.enqueueBuffer(inBuffer, inBuffer.getPacketDescriptions());
  }

  private void receive(byte[] ba)
  {
    if (mRunning) try { mPOS.write(ba); } catch (Exception x) { x.printStackTrace(); }
  }

  public void startRecording() throws Exception
  {
    AudioStreamBasicDescription asbd = new AudioStreamBasicDescription(mSampleRate, mFormatID, new AudioFormatFlags((long)mFormatFlags), mBytesPerPacket, mFramesPerPacket, mBytesPerFrame, mChannelsPerFrame, mBitsPerChannel);
    AudioQueuePtr mQueuePtr = new AudioQueuePtr();
    AudioQueueBufferPtr mBuffers = Struct.allocate(AudioQueueBufferPtr.class, kNumberBuffers);
    System.out.println(11);
    AQRecorderState aqData = new AQRecorderState(this);
    mStateID = aqData.mID();
    System.out.println(12);
    Method callbackMethod = null;
    Method[] methods = this.getClass().getMethods();
    int i = methods.length;
    while (i-->0) if (methods[i].getName().equals("callbackMethod"))
    {
      callbackMethod = methods[i];
      break;
    }
    FunctionPtr fp = new FunctionPtr(callbackMethod );
    VoidPtr vp = aqData.as(VoidPtr.class);

    //AudioErrorCode aqe = AudioQueue.createInput(asbd, fp, vp, null, null, 0, mQueuePtr);
    System.out.println(CFRunLoopMode.Common.value());
    //System.out.println(aqe.name());
    mQueue = mQueuePtr.get();
    int bufferByteSize = deriveBufferSize(mQueue, asbd, 0.5);
    System.out.println("BUFFER SIZE: "+bufferByteSize);

    AudioQueueBufferPtr[] buffers = mBuffers.toArray(kNumberBuffers);
    for (i = 0; i < kNumberBuffers; ++i)
    {
      mQueue.allocateBuffer(bufferByteSize);
      mQueue.enqueueBuffer(buffers[i].get(), buffers[i].get().getPacketDescriptions());
    }

    mRunning = true;
    mQueue.start(null);
  }

}