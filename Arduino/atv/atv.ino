/**
 * Arduino code for mobility chair
 * 
*/

// length of serial input buffer
#define LENGTH 100

// input declaration
int key1;
int key2;

int brake1;
int brake2;

int motor1;
int motor2;

int relay_direction1;
int relay_direction2;

String state="Stop";
String left_b_state="Off";
String right_b_state="Off";

bool left_button = 0;
bool right_button = 0;


int left_button_state;
int right_button_state;

// speed 0-255
int max_speed = 255;
int speed_percentage = 0;
int current_speed = 0; // in percentage

int _key = 0;
int _brake = 0;
int _state = 0;



int percent_to_speed(){
  return max_speed * speed_percentage/100;
}
void start_engine(){
  digitalWrite(key1, 1);
  digitalWrite(key2, 1);
}
void stop_engine(){
  digitalWrite(key1, 0);
  digitalWrite(key2, 0);
  current_speed = 0;
  state = "Stop";
}

void brake_on_1(){
  digitalWrite(brake1, 1);
}
void brake_on_2(){
  digitalWrite(brake2, 1);
}
void brake_on(){
  brake_on_1();
  brake_on_2();
  current_speed = 0;
  state = "Stop";
}

void brake_off_1(){
  digitalWrite(brake1, 0);
}

void brake_off_2(){
  digitalWrite(brake2, 0);
}

void brake_off(){
  brake_off_1();
  brake_off_2();
}

void write_speed(int _speed){
  analogWrite(motor1, _speed);
  analogWrite(motor2, _speed);
}

void move_stop(){
  digitalWrite(relay_direction1, 1);
  digitalWrite(relay_direction2, 1);
  state = "Stop";
}
void move_forward(){
  if(current_speed == 0){
    move_stop();
  }else{
    digitalWrite(relay_direction1, 0);
    digitalWrite(relay_direction2, 1);
    state = "Forward";  
  }
}

void move_backward(){
  if(current_speed == 0){
    move_stop();
  }else{
    digitalWrite(relay_direction1, 1);
    digitalWrite(relay_direction2, 0);
    state = "Backward";  
  }
}
void setup() {
  // put your setup code here, to run once:
  pinMode(left_button, OUTPUT);
  pinMode(right_button, OUTPUT);
  pinMode(key1, OUTPUT);
  pinMode(key2, OUTPUT);
  pinMode(brake1, OUTPUT);
  pinMode(brake2, OUTPUT);
  pinMode(relay_direction1, OUTPUT);
  pinMode(relay_direction2, OUTPUT);
  Serial.begin(115200);
  move_stop();
  brake_on();
}
// string delimiter
int delimit_string(String data, char separator, int index)
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

bool validate_bool(int data){
  return (data==0 || data == 1);
}

void loop() {
   /**
    * input types
    * 1#1#1#20
    * input delimits with "#"
    * first params is key: 1 is key on, 0 is key off
    * second params is brake: 1 is brake on, 0 is brake off
    * third params is directional: 1 is backwards, 0 is forwards 
    * fourth params is speed percentage: 0-100% rest flushes speed to 0
    */
   if (Serial.available() > 0) {
    char buffer[LENGTH];
    int index = 0;
    bool receiving = true;

    while (receiving) {
      if (Serial.available()) {
        char ch = Serial.read();
        if (ch == '\n' || ch == '\0') {
          buffer[index] = '\0';
          receiving = false;
        } else {
          buffer[index++] = ch;
          if (index == LENGTH) {
            buffer[index] = '\0';
            break;
          }
        }
      }
    }
    
    _key = delimit_string(buffer, '#', 0);
    _brake = delimit_string(buffer, '#', 1);
    _state = delimit_string(buffer, '#', 2);
    speed_percentage = delimit_string(buffer, '#', 3);
  }
    
  left_button_state = digitalRead(left_button);
  right_button_state = digitalRead(right_button);
  
  if(!validate_bool(_key)){
    _key = 0;
  }
  if(!validate_bool(_brake)){
    _brake = 0;
  }
  if(!validate_bool(_state)){
    _state = 0;
  }
  
  if(speed_percentage >100 || speed_percentage < 0){
    speed_percentage = 0;
  }
  
  current_speed = percent_to_speed();

  // output
  if(_key == 1){
    start_engine();
  }else{
    stop_engine();
  }
  
  if(_brake == 1){
    brake_on();
  }else{
    brake_off();
  }

  if(_state == 0){
    move_forward();
  }else{
    move_backward();
  }
  
  if(left_button_state == 1){
    brake_off_1();
    left_b_state = "On";
  }else{
    brake_on_1();
    left_b_state = "Off";
  }

  if(right_button_state == 1){
    brake_off_2();
    right_b_state = "On";
  }else{
    brake_on_2();
    right_b_state = "Off";
  }

  
  write_speed(current_speed);
  
  Serial.print(_key);Serial.print("\t");
  Serial.print(_brake);Serial.print("\t");
  Serial.print(_state);Serial.print("\t");
  Serial.print(current_speed);Serial.print("\t");
  Serial.print(state);Serial.print("\t");
  Serial.print(left_button_state);Serial.print(" ");Serial.println(left_button_state);

  //  debug
  delay(500);
  
}
