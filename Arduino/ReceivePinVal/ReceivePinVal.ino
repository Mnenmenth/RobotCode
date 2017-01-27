const int left_m_dir = 2;
const int left_m_brake = 4;
const int left_m_speed = 3;

void setup() {
  pinMode(2, OUTPUT);
  pinMode(3, OUTPUT);
  digitalWrite(2, HIGH);
  Serial.begin(9600);
}

#define CMD_SIZE 6

void loop() {
  char cmd[CMD_SIZE + 1];
  byte size = Serial.readBytes(cmd, CMD_SIZE);
  cmd[size] = 0;

  if(size >= 6) {
    char* tok = strtok(cmd, " ");
    char pinNum[2] = {tok[0], tok[1]};
    tok = strtok(NULL, " ");
    char pinVal[4] = {tok[0], tok[1], tok[2]};
    //Serial.println(atoi(pinNum));
    //Serial.println(atoi(pinVal));
    analogWrite(atoi(pinNum), atoi(pinVal));  
  }
}
