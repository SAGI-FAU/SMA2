package com.sma2.sma2.FeatureExtraction.Movement;

import android.util.Log;
import com.sma2.sma2.FeatureExtraction.Speech.tools.array_manipulation;
import com.sma2.sma2.FeatureExtraction.Speech.tools.f0detector;
import com.sma2.sma2.FeatureExtraction.Speech.tools.sigproc;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.abs;
import static java.util.Arrays.copyOfRange;
import android.os.Environment;
import com.opencsv.CSVWriter;


public class MovementProcessing {


    private float winlen = 3;//Default window length 40ms
    private float winstep = 0.5f;//Default window step 30ms;
    private float maxf0=50;
    private float minf0=  0.3f;
    private int inilag, endlag;
    private sigproc SG = new sigproc();
    private f0detector f0cal = new f0detector();
    private array_manipulation arrays=new array_manipulation();
    double low_th=0.03;
    float vocingthres = 0.45f;
    private float unvocingthres = 0.3f;

    public MovementProcessing(){

    }


    public List<Double> RemoveGravity(List<Double> signal){

        double alpha = 0.8;
        double gravity=0.0;
        List<Double> SignalOut=new ArrayList<>();
        for (int j=0;j<signal.size();j++){
            gravity=alpha * gravity + (1 - alpha) * signal.get(j);
            SignalOut.add(signal.get(j)- gravity);
        }
        return SignalOut;
    }


    public Double ComputePower (List<Double> signal){

        Double Power=0.0;
        for (int j=0;j<signal.size();j++){
            Power+=Math.pow(signal.get(j),2);
        }
        return Power/(double)signal.size();
    }


    public List<Double> getAccR(List<Double> accX, List<Double> accY, List<Double> accZ){
        List<Double> accR=new ArrayList<>();
        double accxi, accyi, acczi;
        for (int j=0;j<accX.size();j++){
            accxi=Math.pow(accX.get(j),2);
            accyi=Math.pow(accY.get(j),2);
            acczi=Math.pow(accZ.get(j),2);
            accR.add(Math.sqrt(accxi+accyi+acczi));
        }
        return accR;
    }

    public Double ComputeTremor(List<Double> AccX, List<Double> AccY, List<Double> AccZ){


        List<Double> AccXn, AccYn, AccZn, AccR;

        AccXn=RemoveGravity(AccX);
        AccYn=RemoveGravity(AccY);
        AccZn=RemoveGravity(AccZ);

        AccR=getAccR(AccXn, AccYn, AccZn);
        double power= ComputePower(AccR);

        return tremortoperc(power);


    }

    public double tremortoperc(double distance){
        return 200/(1+Math.exp(0.2*distance));
    }


    public float[] ComputeF0(List<Double> AccX, List<Double> AccY, List<Double> AccZ, int fs){
        List<Double> AccXn, AccYn, AccZn, AccR;

        inilag = (int) Math.ceil(fs/maxf0);//lower bound lag
        endlag  = (int) Math.ceil(fs/minf0);//upper bound lag
        AccXn=RemoveGravity(AccX);
        AccYn=RemoveGravity(AccY);
        AccZn=RemoveGravity(AccZ);

        AccR=getAccR(AccXn, AccYn, AccZn);

        //This function normalize the speech signal.

        //Get f0 contour from signal
        //Log.e("F0", "Inicia");
        float[] AccR2 = arrays.listtofloat(AccR);
        float[] pitchC =  {0f,0f};//f0cal.sig_f0(AccR2,fs,winlen,winstep,minf0,maxf0);
        //float[] pitchC = f0_cont(AccR, fs);

        //Log.e("F0", "Listo");

        //Fix contour.
        //pitchC = fixf0(pitchC);

        //Set pitch zero for windows less than the analysis window
        //pitchC = zerof0(pitchC);

        //Interpolate missing values
        //pitchC = interpf0(pitchC);


        //Voiced
        //List VoicedSeg = voiced(pitchC,sig, (int) (winstep*fs));

        return pitchC;
    }


    public float perc_stab_freq(float[] f0) {
        //Ensure nonzero values in f0 contour
        List lf0 = arrays.find(f0,0f,2);
        //Convert list to float array
        //List to array
        float[] newF0 = new float[lf0.size()];
        for(int i=0;i<lf0.size();i++)
        {
            newF0[i] = f0[(int)lf0.get(i)];
        }
        f0 = newF0;
        int N = f0.length;
        if (N<4){
            return 100f;
        }
        //Find Max
        float[] temp = Arrays.copyOfRange(f0,0, f0.length);
        Arrays.sort(temp);
        float Mp = temp[temp.length - 1];//Maximum pitch
        //Array with variations between elements of array
        float jitt = 0f;
        float avgjitt=0f;
        int nf=0;
        for (int i = 0; i < N-3; i++)
        {
            jitt=0;
            for (int j=i;j<i+3;j++){
                jitt+= abs(f0[j] - Mp);
            }
            avgjitt+= (100*jitt)/(N*Mp);
            nf++;

        }
        return avgjitt/nf;
    }


    //-----------------------------NEW FUNCTIONS----------------------------------------------------
    public List<Double> simpleIntegral(List<Double> signal){
        double Io=0.0;
        List<Double> SignalOut=new ArrayList<>();
        for(int j=0;j<signal.size()-1;j++){
            Io=0.5*(signal.get(j)+signal.get(j+1))+Io;
            SignalOut.add(Io);
        }
        return SignalOut;
    }

    public List<Double> removeTrends(List<Double> signal){
        List<Double> SignalOut=new ArrayList<>();
        double aux;
        double[] a=new double[2];
        double[] x=new double[signal.size()];
        double[] y=new double[signal.size()];
        for (int j=0;j<signal.size();j++) {
            x[j]=j;
            y[j]=signal.get(j);
        }
        a=LinearRegression(x,y);
        for(int i=0; i<signal.size();i++) {
            aux=signal.get(i)-a[0]-a[1]*i;
            SignalOut.add(aux);
        }
        return SignalOut;
    }

    public List<Double> medianfilt1d(List<Double>signal1d, int N){
        float[] signal = new float[signal1d.size()];
        for(int k=0;k<signal1d.size();k++) {
            signal[k]=signal1d.get(k).floatValue();
        }

        int M=(int)(Math.floor(N/2));
        int beg, end;
        float[] temp;
        List<Double> SignalOut=new ArrayList<>();
        for(int j=0;j<signal.length-1;j++){
            if (j<N || j>signal.length-N) {
                SignalOut.add((double)signal[j]);
            }
            else {
                beg=j-M;
                end=j+M+1;
                temp = Arrays.copyOfRange(signal,beg,end);
                Arrays.sort(temp);
                SignalOut.add((double)(temp[M]));
            }
        }
        return SignalOut;
    }

    public double[] LinearRegression(double[] x, double[] y) {
        double slope, intercept;
        int n = x.length;
        double[] a=new double[2];
        // first pass
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < n; i++) {
            sumx += x[i];
            sumx2 += x[i] * x[i];
            sumy += y[i];
        }
        double xbar = sumx/n;
        double ybar = sumy/n;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        slope = xybar/xxbar;
        intercept = ybar - slope * xbar;
        a[0]=intercept;
        a[1]=slope;
        return a;
    }

    public double TremorMov(List<Double> AccX, List<Double> AccY, List<Double> AccZ){
        List<Double> AccXn, AccYn, AccZn, AccR, AccFilter;
        List<Double> AccNoise=new ArrayList<>();
        double aux=0;

        AccXn=RemoveGravity(AccX);
        AccYn=RemoveGravity(AccY);
        AccZn=RemoveGravity(AccZ);

        AccR=getAccR(AccXn, AccYn, AccZn);
        AccFilter=medianfilt1d(AccR,50);
        for(int k=0;k<AccFilter.size();k++) {
            aux=AccR.get(k) - AccFilter.get(k);
            AccNoise.add(aux);
        }
        return 100/(1+ComputePower(AccNoise));
    }


    // Conversion de ArrayList<Double> to ArrayList<Float>
    public ArrayList<Float> getAccX(List<Double> signal){
        List<Double> AccXn;
        AccXn = RemoveGravity(signal);
        ArrayList<Float> SignalOut = new ArrayList<>();
        for (int j = 600; j < signal.size(); j++){
            SignalOut.add(AccXn.get(j).floatValue());
        }
        return SignalOut;
    }

    public ArrayList<Float> getTime(List<Float> signal){
        ArrayList<Float> SignalOut = new ArrayList<>();
        float temp = 0.0f;
        for (float j = 0; j < signal.size(); j++){
            temp = j/100;
            SignalOut.add(temp);
        }
        return SignalOut;
    }

    // Put the feature extraction code in the following functions
    public double getStepTime(List<Integer> stepIndex) {
        List<Double> stepTimes = new ArrayList<Double>();
        for(int i = 1; i < stepIndex.size(); i++) {
            stepTimes.add((stepIndex.get(i) - stepIndex.get(i-1))/100.0);
        }
        // convert List to array and calculate median
        return new Median().evaluate(ArrayUtils.toPrimitive(stepTimes.toArray(new Double[stepTimes.size()])));
    }

    public double velocity(List<Float> signal){
        return 2;
    }

    public int stability(List<Float> signal1, List<Float> signal2){

        return 10;
    }



    public Double ComputePower2 (float[] signal){
        Double Power=0.0;
        for (int j=0;j<signal.length;j++){
            Power+=Math.pow(signal[j],2);
        }
        return Power/(double)signal.length;
    }


    public float [] sigmoid(float [] sig){
        float [] sig_sigmoid= new float[sig.length];
        for (int j=0;j<sig.length;j++){
            sig_sigmoid[j]= (float) (1/(1+Math.exp(-sig[j])));

        }

        return sig_sigmoid;
    }

    public List<Double> removeInitGait(List<Double> sig, int Fs, double winSize, double winStep){

        array_manipulation Arr_man= new array_manipulation();
        List<Double> newSig= new ArrayList<>();

        float[] sig_array = Arr_man.dlisttoarrayF(sig);

        sigproc SigProc= new sigproc();
        List <float[]> AccR_framed = SigProc.sigframe(sig_array,Fs, (float) winSize, (float) winStep);
        List <Double> PowerArr = new ArrayList<>();
        for(int i=0;i<AccR_framed.size();i++)
        {
            Double Power = ComputePower2(AccR_framed.get(i));
            PowerArr.add(Power);
        }

        float[] Power_Array = Arr_man.dlisttoarrayF(PowerArr);

        float [] sigmoid=sigmoid(Power_Array);

        float temp=sigmoid[0];
        int index=0;
        for(int i=1;i<sigmoid.length;i++){

            if(sigmoid[i]<temp){
                index=i;
                temp=sigmoid[i];
            }


        }
        index=Math.round((float) (Fs*index*winStep));

        for(int i=index;i<sig.size();i++) {

            newSig.add(sig.get(i));


        }




        return  newSig;
    }



    public float freezeIndex(List<Double> sig,List<Double> oldTime, int Fs)
    {

        sigproc SigProc=new sigproc();
        LinearInterpolation linearInterpolation= new LinearInterpolation();
        List<Double> newTime = new ArrayList<>();
        List<Double> AccR= new ArrayList<>();
        //Extracting Time Stamp


        //Old time is given in ns, we have to converted to ms

        for (int i = 0; i < oldTime.size(); i++) {

            newTime.add(oldTime.get(i)*Math.pow(10,-9));
        }



        AccR= linearInterpolation.interpolateLinearToSamplingRate(sig, newTime, Fs);

        array_manipulation arrayMan= new array_manipulation();

        double [] Signal=arrayMan.dlisttoarray(AccR);
        //sigproc SG= new sigproc();


        //double [] sig_win=SG.makeWindow(Signal,1);

        int zpad = 2 * Signal.length;
        //Append half a window length of zeroes
        while ((zpad & (zpad - 1)) != 0)//Append zeroes until the number of samples is a power of two
        {
            zpad = zpad + 1;
        }
        double [] sig_win=new double[zpad];


        for (int i = 0; i < AccR.size(); i++) {

            sig_win[i] = (double) Signal[i];

        }




        for (int i = Signal.length; i < zpad; i++) {

            sig_win[i] = 0;

        }



        FastFourierTransformer fft1= new FastFourierTransformer(DftNormalization.STANDARD);
        Log.e("gait", "señal orig"+String.valueOf(sig_win.length));
        Complex[] spect = fft1.transform(sig_win, TransformType.FORWARD);
        Log.e("gait", "señal fft"+String.valueOf(spect.length));


        // Norm of the spect

        float [] sig_fft=new float[spect.length/2];
        for (int i = 0; i < (spect.length/2); i++) {

            sig_fft[i]=(float) Math.sqrt(Math.pow(spect[i].getReal(),2)+ Math.pow(spect[i].getImaginary(),2));
        }


        // Bins to Freqs

        // Each bin contains
        double freqtoBin=  (Fs/2.0)/sig_fft.length;

        //To compute the Freeze index we need two bands
        // Locomotor Band: 0.5 to 3 Hz
        int nBinsLB_init=(int) Math.round(0.5/freqtoBin);
        int nBinsLB_end=(int) Math.round(3.0/freqtoBin);
        int lenLB=nBinsLB_end- nBinsLB_init;


        float [] powerLB=new float[nBinsLB_end- nBinsLB_init];
        for (int i = nBinsLB_init; i < (nBinsLB_end); i++) {
            powerLB[i-nBinsLB_init] = (float) (Math.pow(abs(sig_fft[i]), 2))/lenLB;
        }

        // Freeze band 3 to 8 Hz

        int nBinsFB_init=(int) Math.round(3/freqtoBin);
        int nBinsFB_end=(int) Math.round(8/freqtoBin);
        int lenFB=nBinsFB_end- nBinsFB_init;

        float [] powerFB=new float[nBinsFB_end- nBinsFB_init];
        for (int i = nBinsFB_init; i < (nBinsFB_end); i++) {
            powerFB[i-nBinsFB_init] = (float) (Math.pow(abs(sig_fft[i]), 2))/lenFB;
        }

        float powerFBM=SigProc.meanval(powerFB);
        float powerLBM=SigProc.meanval(powerLB);

        float freezeI=powerFBM/powerLBM;



        return freezeI;
    }

    public CSVFileReader.Signal getAccNorm(CSVFileReader.Signal accX, CSVFileReader.Signal accY, CSVFileReader.Signal accZ) {
        return new CSVFileReader.Signal(accX.TimeStamp,getAccR(accX.Signal,accY.Signal,accZ.Signal));
    }


    // New movement functions for superior members

    public float UppTremor(CSVFileReader FileReader,String path_movement){
        double vtremor=0;
        double Tremor=0;

        if(path_movement==null){
            vtremor=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement, "aZ [m/s^2]");
            Tremor = ComputeTremor(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
            vtremor = 100-Tremor;

        }

        return (float) vtremor;
    }

    public float UppRegularity(CSVFileReader FileReader,String path_movement){
        double stabVel=0;
        if(path_movement==null){
            stabVel=0;
        }
        else {
            CSVFileReader.Signal TremorSignalaX2 = FileReader.ReadMovementSignal(path_movement, "aX [m/s^2]");
            CSVFileReader.Signal TremorSignalaY2 = FileReader.ReadMovementSignal(path_movement, "aY [m/s^2]");
            CSVFileReader.Signal TremorSignalaZ2 = FileReader.ReadMovementSignal(path_movement, "aZ [m/s^2]");
            stabVel = ComputeRegularity(TremorSignalaX2.Signal, TremorSignalaY2.Signal, TremorSignalaZ2.Signal);
        }
        return (float) stabVel;
    }


    public Double ObtainValue(Double Signal){
        Double value= 0.0;

        value = 200/(1+Math.exp(5*Signal));
        return value;
    }


    public double ComputeRegularity(List<Double> AccX, List<Double> AccY, List<Double> AccZ){
        sigproc sigproccess = new sigproc();

        List<Double> AccXn, AccYn, AccZn, AccR;

        AccXn=RemoveGravity(AccX);
        AccYn=RemoveGravity(AccY);
        AccZn=RemoveGravity(AccZ);

        AccR=getAccR(AccXn, AccYn, AccZn);

        float[] AccR_aux = arrays.listtofloat(AccR);
        int Fs=100;
        //Divide the signal into frames
        List <float[]> AccR_framed = sigproccess.sigframe(AccR_aux,Fs,(float) 0.4,(float) 0.02);
        List <Double> PowerArr = new ArrayList<>();
        for(int i=0;i<AccR_framed.size();i++)
        {
            Double Power = ComputePower2(AccR_framed.get(i));
            PowerArr.add(Power);
        }

        float[] Power_Array = arrays.listtofloat(PowerArr);
        double Mean_Power = sigproccess.meanval(Power_Array);

        int start = 400;
        List<Float> New_Power = new ArrayList<>();

        //Remove noise data: only movement data
        for(int i=0;i<Power_Array.length;i++)
        {
            if ((i>=start) && ((Power_Array[i])>=Mean_Power)){
                New_Power.add(Power_Array[i]);
            }
        }

        //Convert the List to float Array
        float[] New_PowerArray = new float[New_Power.size()];
        for (int i = 0; i < New_Power.size(); i++) {
            New_PowerArray[i] = New_Power.get(i);
        }

        int w=3;//number of neighbor points
        double stdnew = sigproccess.calculateSD(New_PowerArray);

        List<Float> Time_Vector = new ArrayList<>();
        Time_Vector.add(((float) 0/ (float) Fs));

        for(int i=0;i<New_PowerArray.length;i++)
        {
            if((i>=w)&&(i<=New_PowerArray.length - w - 1)){
                if((New_PowerArray[i]<New_PowerArray[i-3])&&
                        (New_PowerArray[i]<New_PowerArray[i-2])&&
                        (New_PowerArray[i]<New_PowerArray[i-1])&&
                        (New_PowerArray[i]<New_PowerArray[i+1])&&
                        (New_PowerArray[i]<New_PowerArray[i+2])&&
                        (New_PowerArray[i]<New_PowerArray[i+3])&&
                        (New_PowerArray[i]<(Mean_Power+stdnew))){
                    Time_Vector.add(((float) i/ (float) Fs));
                }
            }

        }
        Time_Vector.add(((float)(New_PowerArray.length - 1)/(float)Fs));

        //Convert to float array
        float[] Time_Vec_Array = new float[Time_Vector.size()];
        for (int i = 0; i < Time_Vector.size(); i++) {
            Time_Vec_Array[i] = Time_Vector.get(i);
        }

        //Compute the difference between 2 consecutive points
        float[] Final_Time_Array = new float[Time_Vec_Array.length - 1];
        for(int i=1;i<Time_Vec_Array.length;i++) {
            Final_Time_Array[i-1] = Time_Vec_Array[i] - Time_Vec_Array[i-1];
        }

        double stdtime = sigproccess.calculateSD(Final_Time_Array);

        double StandarDeviation = ObtainValue(stdtime);
        return (StandarDeviation);
    }

    public void export_movement_feature(String name_file, float feature, String feat_name) throws IOException {
        String directory = Environment.getExternalStorageDirectory() + "/Apkinson/FEATURES/";
        String fileName = directory + feat_name+".csv";


        File direct = new File(directory);
        if (!direct.exists()) {
            direct.mkdirs();
        }
        File file =new File(fileName);

        CSVWriter writer;
        FileWriter mFileWriter;
        try {
            if(file.exists() && !file.isDirectory()){
                mFileWriter = new FileWriter(fileName , true) ;
                writer = new CSVWriter(mFileWriter);
            }
            else {
                writer = new CSVWriter(new FileWriter(fileName));
                String[] header={"File", feat_name};
                writer.writeNext(header);
            }
            String name=name_file.substring(name_file.lastIndexOf("/")+1);
            String[] row={"",""};
            row[0]=name;
            row[1]=Float.toString(feature);

            writer.writeNext(row);
            writer.close();
        } catch(IOException ie) {
            ie.printStackTrace();
        }


    }



    
}
