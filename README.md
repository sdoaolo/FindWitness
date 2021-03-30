# FindWitness ( 사고 속 목격자 찾기 )

- 2020 소프트웨어 개발보안 시큐어코딩에서 진행한 프로젝트입니다. 

### 기능 소개
- 사용자 GPS 기록 저장
- 사고당시 날짜 시간 조회를 통해 사용자가 GPS의 장소를 선택
- 현재 GPS 정보 알기 
- 목격자일 가능성이 존재하는 이용자 목록 검색
- 이용자와 사고 당사자간 개인정보 노출 없이 1대1 채팅 
- 교통사고 통계 열람

### 사용 스택
- Java(Android, client)
- JavaScript (채팅 Server)
- Figma(client, UI, Prototype 제작)
- HTTPUrlConnection (Client, 통신)
- socket (Client, 통신)
- SQLite (Database)
- MySQL (Database)
- NodeJS Express (Server)
- Github (버전관리)

### 소개

> 서비스 개요
- 도로교통공단 교통사고 추세에 따르면, 2019년 229600 건 (매일 600건 이상)이다.
- 예고없이 발생하는 교통사고는 많은 사람들에게 고통과 피해를 입힌다.
- 최근 자동차 급발진 사고영상에 따르면, 목숨이 오고 가는 상황이었음에도 불구하고 차주는 자작극이 아님을 증명해야 했다. 
- 사고 당사자는 사고 후에도 자신의 결백을 입증하거나, 과실 판단을 위하여 증거(목격자, CCTV,블랙박스 등)를 모아야 한다.

> 서비스 기획
- 현재 사고가 일어났을 때 목격자를 찾는 방법
1) 신문, SNS에 제보한다.
2) 사고 차량의 블랙박스 영상에 찍힌  다른 차들의 번호판을 이용한다.

- 현재 방법들의 문제점
1) 목격자 제보 가능성이 불투명하다.
2) 목격자가 자신이 목격자임을 자각하지 못한 경우 개인정보보호법에 의해 차번호를 알아도 연락이 닿기 어려울 수 있다.

 사고 속에서 목격자가 될 가능성이 있는 사람들을 빠르고 쉽게 찾아주어 연결시켜주는 서비스(앱)를 기획

> 차별성
- 시중의 GPS 기반 서비스는 교통 상황(원활, 침체) 알리미와 같이 오직 정보만을 제공함.
- '사고속 목격자 찾기' 는 gps 기반으로 사고 발생 당시의 정보를 저장하여 후에 사고 당시 주변을 지나갔던 목격자들을 찾을 수 있도록 도와준다.
또 개인정보의 교환 없이도 채팅 할 수 있어  상호 커뮤니케이션이 가능하다.

> 기대효과
-사용자가 쉽게 교통사고 관련 통계자료에 접할 수 있다.
- 통계자료를 통해 경각심을 심어 줌으로서 사용자의 안전 운전을 독려한다.
- 증거를 찾지 못해서 정신적, 정서적으로 불안정하고 위험할 수 있는 상황에서 벗어날 수 있다.
- 교통사고가 발생 후엔 이미 신체적으로 안전하지 못한 상황이기 때문에 정신적인 안전(편안하고 온전한 상태)를 유지 할 수 있다는 것은 국민들에게 큰 힘이 될 것이다.

### 서비스 이용 대상
![서비스 대상자](https://user-images.githubusercontent.com/48430781/112985984-e271cf80-919b-11eb-8366-847d0c726b77.PNG)

> 특장점
- 편리한 목격자 검색 기능
- 사고 차량의 운전자나 동승자가 목격자와 증거 영상을 찾아야 할 때, 어플리케이션의 검색 서비스를 통해 목격자일 가능성이 있는 이들을 연결 받음으로써 더 쉽게 연락을 주고받을 수 있다.

> 안전 채팅 서비스
별다른 개인정보를 공유하지 않아도 어플리케이션 내에서 목격자와 사고 당사자를 연결해줌으로써 통신이 가능하도록 해준다. 만약 추후 별도의 연락이 필요하면. 어플리케이션 내의 기능을 그대로 이용해도 되며 서로의 합의하에 개인정보를 공유한 뒤 별도로 연락 가능하다



### 견본 이미지
![Android - 16](https://user-images.githubusercontent.com/48430781/112990567-2f0bd980-91a1-11eb-91e7-959a43441904.png)
![Android - 17](https://user-images.githubusercontent.com/48430781/112990572-303d0680-91a1-11eb-8440-021266265622.png)
![Android - 18](https://user-images.githubusercontent.com/48430781/112990574-303d0680-91a1-11eb-8eb4-2043bf7107e9.png)
![Android - 19](https://user-images.githubusercontent.com/48430781/112990576-30d59d00-91a1-11eb-966a-434e3c8fa0fd.png)
![Android - 22](https://user-images.githubusercontent.com/48430781/112990579-316e3380-91a1-11eb-8be7-209e1c855403.png)
![Android - 23](https://user-images.githubusercontent.com/48430781/112990581-3206ca00-91a1-11eb-9842-13b48323256a.png)
![Android - 24](https://user-images.githubusercontent.com/48430781/112990583-3206ca00-91a1-11eb-9eae-3d5dacbd9eb8.png)
![Android - 26](https://user-images.githubusercontent.com/48430781/112990584-329f6080-91a1-11eb-80e5-ae3598b6ea5e.png)
![Android - 30](https://user-images.githubusercontent.com/48430781/112990586-3337f700-91a1-11eb-9660-7fb32df81cdd.png)
![Android - 31](https://user-images.githubusercontent.com/48430781/112990591-33d08d80-91a1-11eb-8fa3-46ceede8504e.png)


