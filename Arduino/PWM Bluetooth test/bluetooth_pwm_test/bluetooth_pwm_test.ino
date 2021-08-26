#include <SoftwareSerial.h>
//SoftwareSerial BTserial(10, 11); // RX |
#include <AltSoftSerial.h>

AltSoftSerial BTserial; // 9 RX | 8 TX

int max_volt = 170;
int LENGTH = 10;
int LEDPin1 = 3;
int LEDPin2 = 5;
int LEDPin3 = 6;
int LEDPin4 = 11;

int myTimeout = 50;

int _speed1 = 0;
int _speed2 = 0;

void setup() {

  BTserial.begin(9600);
  Serial.begin(9600);
  pinMode(LEDPin1, OUTPUT);
  pinMode(LEDPin2, OUTPUT);
  pinMode(LEDPin3, OUTPUT);
  pinMode(LEDPin4, OUTPUT);
}

// function to delimit the string
int getValue(String data, char separator, int index)
{
  int found = 0;
  int strIndex[] = { 0, -1 };
  int maxIndex = data.length() - 1;

  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]).toInt() : -1;
}

void loop ()
{
  
  BTserial.print("g,1,0,100,0,1,200,g#");
  Serial.println(BTserial.available());
  if (BTserial.available()) {
    bool r = true;
    String buffer = "";
    while(r){
      char buf = (char)BTserial.read();
      if(buf == '\n'){
        r = false;
        break;
      }else{
         if(isDigit(buf) || buf == '#' || buf == '-'){
          buffer.concat(String(buf)); 
         }
      }   
    }
    Serial.println(buffer);
    int _speed1 = getValue(buffer, '#', 0);
    int _speed2 = getValue(buffer, '#', 1);
    
    Serial.print(_speed1);Serial.print(" ");Serial.println(_speed2);
    _speed1 = _speed1 != -1 ? _speed1 : 0;
    _speed2 = _speed2 != -1 ? _speed2 : 0;

    if (_speed1 > 0)
    {
      analogWrite(LEDPin1, map(_speed1, 0, 100, 0, 170));
      analogWrite(LEDPin2, 0);
    } else if (_speed1 < 0) {
      analogWrite(LEDPin2, map(_speed1, 0, -100, 0, 170));
      analogWrite(LEDPin1, 0);
    } else {
      analogWrite(LEDPin1, 0);
      analogWrite(LEDPin2, 0);
    }
//
//
     if (_speed2 > 0)
    {
      analogWrite(LEDPin3, map(_speed2, 0, 100, 0, 170));
      analogWrite(LEDPin4, 0);
    } else if (_speed2 < 0) {
      analogWrite(LEDPin4, map(_speed2, 0, -100, 0, 170));
      analogWrite(LEDPin3, 0);
    } else {
      analogWrite(LEDPin4, 0);
      analogWrite(LEDPin3, 0);
    }
  }
  
}
