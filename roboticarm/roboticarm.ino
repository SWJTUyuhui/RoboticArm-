
#include <Servo.h>
#include <FlexiTimer2.h>
#include "floatToString.h"
#include <arduino.h>
#include <stdlib.h>
#define FIRST_SERVO_PIN 2 
#define l1 18
#define l2 20
float dx2,dy2,e,afa=3.14,sina,cosa;
int x,y;

Servo armServo[4]; // max servos is 48 for mega, 12 for other boards
int servoPins[4];
int servopwm[4]={90,90,90,90};
int lastservopwm[4]={90,90,90,90};
int nowservopwm[4]={90,90,90,90};
String pos,pos1,posdata1="200",posdata2="0",posdata3="0",posdata4="500",posdata5="500",posdata6="500";
char get,charreceive,command,posdata;
int mark,poss,posx,posy,flag,len;
//int speed4pin=4;
int speed1pin=5;
int speed2pin=6;
int speed3pin=7;

int speed1dir1=A4;
int speed1dir2=A5;
int speed2dir1=A2;
int speed2dir2=A3;
int speed3dir1=A0;
int speed3dir2=A1;
int temp1,temp2,temp3;
int pwm1,pwm2,pwm3;
int speed1=0,speed2=0,speed3,speed1temp=0,speed2temp=0,speed3temp;
int speed1e3=0,speed1e2=0,speed1e1=0;
int speed2e3=0,speed2e2=0,speed2e1=0;
int speed3e3=0,speed3e2=0,speed3e1=0;
//int speed4e3=0,speed4e2=0,speed4e1=0;
float a=1,b=0.4,c=0;  //车轮闭环PID参数
void speed1PID(int goal,int temp);
void speed2PID(int goal,int temp);
void speed3PID(int goal,int temp);
void speed4PID(int goal,int temp);
void setup()
{ Serial.begin(9600);
  Serial3.begin(9600);
  Serial3.println("mega2560begin");
  Serial.println("mega2560begin");
   pinMode(speed1pin,OUTPUT);//定义该接口为接口为输出接口
   pinMode(speed1dir1,OUTPUT);
   pinMode(speed1dir2,OUTPUT);
   pinMode(speed2pin,OUTPUT);//定义该接口为接口为输出接口
   pinMode(speed2dir1,OUTPUT);
   pinMode(speed2dir2,OUTPUT);
   pinMode(speed3pin,OUTPUT);//定义该接口为接口为输出接口
   pinMode(speed3dir1,OUTPUT);
   pinMode(speed3dir2,OUTPUT);

  servoPins[0] = 31;
  servoPins[1] = 33;
  servoPins[2] = 35;
  servoPins[3] = 37;
  armServo[0].attach(servoPins[0]);
  armServo[1].attach(servoPins[1]);
  armServo[2].attach(servoPins[2]);
  armServo[3].attach(servoPins[3]);
  nowservopwm[0]=90;
  nowservopwm[1]=90;
  nowservopwm[2]=90;
  nowservopwm[3]=90;
  armServo[0].write(nowservopwm[0]);
  armServo[1].write(nowservopwm[1]);
  armServo[2].write(nowservopwm[2]);
  armServo[3].write(nowservopwm[3]);
  
  attachInterrupt(3,speed1cap, FALLING);//20pin
  attachInterrupt(4,speed2cap, FALLING);//19pin
  attachInterrupt(5,speed3cap, FALLING);//18pin
 
  //10msInterrupt
  FlexiTimer2::set(10,follow); // ms period
  FlexiTimer2::start();
      x=20;
      y=10; 
}
void loop()
{ 
 
while(Serial3.available())
   {
     charreceive=char(Serial3.read());
     pos+=charreceive;
    delay(2);
    mark=1;
   }
   charreceive=' ';
   
    if (mark == 1) {
      Serial.println("get:"+pos);
      
      len=pos.length();
     // Serial.println("len"+len);
      delayMicroseconds(10);
      for(int i=0;i<len;i++){
      command=pos[i];
           if(command=='p')posdata1=pos.substring(1+i,4+i);
       if(command=='i')posdata2=pos.substring(1+i,4+i);
       if(command=='d')posdata3=pos.substring(1+i,4+i);
       if(command=='x')posdata4=pos.substring(1+i,4+i);
       if(command=='y')posdata5=pos.substring(1+i,4+i);
       if(command=='w')posdata6=pos.substring(1+i,4+i);
       if(command=='a')servopwm[0]=pos.substring(i+1,4+i).toInt();
       if(command=='b')servopwm[1]=180-pos.substring(1+i,4+i).toInt();  
       if(command=='c')servopwm[2]=pos.substring(1+i,4+i).toInt();
       if(command=='g')if(pos.substring(1+i,2+i).toInt()==1)servopwm[3]=120; else servopwm[3]=30;
       if(command=='u'){servopwm[0]=pos.substring(1+i,4+i).toInt();lastservopwm[0]=servopwm[0];}
       if(command=='k'){servopwm[1]=pos.substring(1+i,4+i).toInt();lastservopwm[1]=servopwm[1];}
       if(command=='o'){servopwm[2]=pos.substring(1+i,4+i).toInt();lastservopwm[2]=servopwm[2];}
       if(command=='p'){servopwm[0]=90;lastservopwm[0]=servopwm[0];servopwm[1]=90;lastservopwm[1]=servopwm[1];servopwm[2]=90;lastservopwm[2]=servopwm[2];servopwm[3]=90;lastservopwm[3]=servopwm[3];}
    }
pos="p"+posdata1+"i"+posdata2+"d"+posdata3+"x"+posdata4+"y"+posdata5+"w"+posdata6+"servopwm[0]"+servopwm[0]+"servopwm[1]"+servopwm[1]+"servopwm[2]"+servopwm[2]+"servopwm[3]"+servopwm[3];
Serial.println(pos);     
   
  if(servopwm[0]<10)servopwm[0]=10;
  if(servopwm[0]>170)servopwm[0]=170;
  if(servopwm[1]<10)servopwm[1]=10;
  if(servopwm[1]>170)servopwm[1]=170;
  if(servopwm[2]<0)servopwm[2]=0;
  if(servopwm[2]>180)servopwm[2]=180;
  if(servopwm[3]<30)servopwm[3]=30;
  if(servopwm[3]>140)servopwm[3]=140;
 
       mark=0; 
       Serial3.println(pos);
       pos="";
       
//       a=posdata1.toInt()/100;//PID
//       b=posdata2.toInt()/100;
//       c=posdata3.toInt()/100;
       posx=posdata4.toInt()-500;//
       posy=posdata5.toInt()-500;
       poss=posdata6.toInt()-500;   
       speed1temp=posy+poss/10; 
     if(speed1temp<0){speed1=-speed1temp;digitalWrite(speed1dir1,LOW);digitalWrite(speed1dir2,HIGH);}else{speed1=speed1temp;digitalWrite(speed1dir1,HIGH);digitalWrite(speed1dir2,LOW);}

       speed2temp=-0.87*posx-0.5*posy+poss/10;
     if(speed2temp<0){speed2=-speed2temp;digitalWrite(speed2dir1,LOW);digitalWrite(speed2dir2,HIGH);}else{speed2=speed2temp;digitalWrite(speed2dir1,HIGH);digitalWrite(speed2dir2,LOW);}
   
       speed3temp=0.87*posx-0.5*posy+poss/10;
     if(speed3temp<0){speed3=-speed3temp;digitalWrite(speed3dir1,LOW);digitalWrite(speed3dir2,HIGH);}else{speed3=speed3temp;digitalWrite(speed3dir1,HIGH);digitalWrite(speed3dir2,LOW);}
      

}
delay(40);
 for(int j=0;j<4;j++){
    if(abs(servopwm[j]-lastservopwm[j])>2)
  nowservopwm[j]=lastservopwm[j]+2*(servopwm[j]>lastservopwm[j]?1:-1);
else nowservopwm[j]=servopwm[j];}
    armServo[0].write(nowservopwm[0]);
    armServo[1].write(nowservopwm[1]);
    armServo[2].write(nowservopwm[2]);
    armServo[3].write(nowservopwm[3]);
    for(int j=0;j<4;j++)lastservopwm[j]=nowservopwm[j];
//      sina=sin(afa);
//      cosa=cos(afa);
//      dx2=x-l1*cos(afa);
//      dy2=y-l1*sin(afa);
//     
//     e=(dx2*dx2+dy2*dy2-l2*l2)/2*l1/(sina*dx2-cosa*dy2);
//     String out=floatToString(echar,afa,4,6);//floatToString(buffer string, float value, precision, minimum text width)
//     if(e<0.2&&e>-0.2)Serial.println("outcome:"+out);
//else {afa=afa-e;Serial.println("working:"+out);}
}
void follow(){
  
//Serial3.println(speed1);
//Serial3.println(temp1);
//Serial3.println("speed1"+speed1);
//Serial3.println("speed2"+speed2);
//Serial3.println("speed3"+speed3);
speed1PID(speed1,temp1);
temp1=0;
speed2PID(speed2,temp2);
temp2=0;
speed3PID(speed3,temp3);
temp3=0;
}
void speed1cap()
{
  temp1++; 
}
void speed2cap()
{
  temp2++; 
}
void speed3cap()
{
  temp3++;  
}

void speed1PID(int goal,int temp)
{
speed1e3= speed1e2;
speed1e2=speed1e1;
speed1e1= goal*2-temp;
pwm1+=a*speed1e1+b*speed1e2+c*speed1e3;
if(pwm1<=0)pwm1=0;
else
if(pwm1>=255)pwm1=255;

//Serial.println(speed1e1);
analogWrite(speed1pin,pwm1);
}

void speed2PID(int goal,int temp)
{
speed2e3= speed2e2;
speed2e2=speed2e1;
speed2e1= goal*2-temp;
pwm2+=a*speed2e1+b*speed2e2+c*speed2e3;
if(pwm2<=0)pwm2=0;
else
if(pwm2>=255)pwm2=255;
analogWrite(speed2pin,pwm2);

}
void speed3PID(int goal,int temp)
{
speed3e3= speed3e2;
speed3e2=speed3e1;
speed3e1=goal*2-temp;
pwm3+=a*speed3e1+b*speed3e2+c*speed3e3;
if(pwm3<=0)pwm3=0;
else
if(pwm3>=255)pwm3=255;
analogWrite(speed3pin,pwm3);

}

