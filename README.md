# O(n)-Logistics: 대규모 물류 관리 시스템
## 📌 프로젝트 소개
SSG-DEAL은 이벤트성 할인 프로모션 특가 행사를 대행하여 판매할 수 있는 이커머스 플랫폼입니다.
최근 네고왕, 또간집 등 인플루언서를 통한 이벤트성 할인 프로모션 특가에 대한 제품 행사가 많이 열립니다.
이를 대행하여 판매할 수 있는 이커머스 플랫폼을 제공하기 위해 기획하게 되었습니다.

</br>
</br>

## 📌 프로젝트 목적
벤치마킹한 유튜브 채널의 업로드 일정과 데이터를 분석한 결과, 2주 간격으로 금요일 오후 6시에 콘텐츠가 업로드되는 패턴을 확인하였고, ‘또간집’, ‘네고왕’과 같은 대형 콘텐츠 업로드 시점에 트래픽이 급증할 것으로 예상했습니다. 
특히 프로모션 시작일을 기준으로 23일 동안 트래픽이 집중되며, 첫날 저녁 18:30-20:00, 둘째 날 점심과 저녁 시간대(12:00-13:00, 18:30-20:00)에 피크가 발생할 것으로 예측했습니다. 
이에 따라 하루 최대 동시 요청자 수는 3만 명, 평균 5천 명 수준을 목표로 설정했으며, 주문/결제 처리 성능은 TPS 200을 달성하는 것을 목표로 삼아 대규모 트래픽을 안정적으로 수용할 수 있는 서비스를 구축하는 것을 주요 목표로 삼았습니다.

</br>
</br>

## 📌 프로젝트 기능 및 기술 적용
### 📄 단기 프로모션 관리 및 대규모 트래픽 감당
> 저희 서비스에서는 단기 프로모션을 진행하는 업체가 해당 서비스로 대행 판매를 요청하게 됩니다.
> 이에 따라 프로모션의 기간, 내용, 업체, 프로모션에 해당되는 상품과 상품 재고 등을 관리하게 됩니다.
> 유저는 프로모션의 기간 동안 정해진 할인 금액으로 상품을 장바구니에 담고, 주문 및 결제를 진행할 수 있습니다.
> 결제가 완료되면 해당 주문이 완료되었다는 알림을 수신할 수 있습니다.
![image](https://github.com/user-attachments/assets/48a896e6-87cc-4f15-8401-116f941929b7)


### 🌱 유저 서비스의 JWT/Passport 도입
> 유저 서비스에서는 분산된 MSA 환경에서도 일관된 인증 체계를 갖추고자 JWT 토큰을 이용하여 게이트웨이에서 검증을 진행하였습니다.
> 그러나 JWT 토큰으로 인증을 진행한 이후로, 타 서비스에서 유저의 상세 정보와 인증 정보를 얻으려면 지속적으로 유저 서비스와 인증 서비스를 호출해야 했고, 이 단점을 줄이고자 게이트웨이에서 JWT 토큰으로 인증을 마치면 내부 통신용 인증 객체인 패스포트 토큰을 발급하였습니다.

</br>
</br>

## 🚨 Trouble Shooting
### Kafka 요청 시 JPA AuditingConfig 에러 [LINK](https://teamsparta.notion.site/Kafka-JPA-AuditingConfig-1e42dc3ef5148046a60bf76bd4b966b4)

</br>
</br>

## 🌐 Architecture
![image](https://github.com/user-attachments/assets/31c1a8aa-67e4-423d-8d34-05c04bb7f8f8)

</br>
</br>

## 🛠️ 기술 스택
### ⚡️Language & Framework
![springboot](https://img.shields.io/badge/spring&nbsp;boot-%236DB33F.svg?style=for-the-badge&logo=springboot&logoColor=white)
![springcloud](https://img.shields.io/badge/spring&nbsp;cloud-%236DB33F.svg?style=for-the-badge&logo=springcloud&logoColor=white)

### 💾 Database 
![](https://img.shields.io/badge/postgresql-4479A1?style=for-the-badge&logo=postgresql&logoColor=white)
![](https://img.shields.io/badge/redis-DD0031?style=for-the-badge&logo=redis&logoColor=white)

### 🐳 Containerization
![](https://img.shields.io/badge/docker-339AF0?style=for-the-badge&logo=docker&logoColor=white)

### 🖥️ Monitoring Tools
![](https://img.shields.io/badge/grafana-%23F46800.svg?style=for-the-badge&logo=grafana&logoColor=white)
![](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=Prometheus&logoColor=white)

### 🛠 Tools
![](https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white)
![](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white)
</br>
</br>

## 👋🏻 팀원 소개
|           윤한나           |           전주호           |                 최동인               |         양현진          |         김소민          |
| :--------------------------------------------------------------: | :--------------------------------------------------------------: | :--------------------------------------------------------------------------: | :-----------------------------------------------------------: |:-----------------------------------------------------------: |
|      <img src="https://avatars.githubusercontent.com/hyper-log" width="120px;" alt=""/>      |      <img src="https://avatars.githubusercontent.com/jeonjuho23" width="120px;" alt=""/>      |            <img src="https://avatars.githubusercontent.com/Bulgogi-Pizza" width="120px;" alt=""/>            |    <img src="https://avatars.githubusercontent.com/woo-lala" width="120px;" alt=""/>     |    <img src="https://avatars.githubusercontent.com/ss0ming" width="120px;" alt=""/>     |
|                            BE / 주문                           |                            BE / 장바구니                         |                                  BE / 인증인가 / 회원 / 프로모션                                |                         BE / 프로모션 / 알림                          |                          BE / 결제                          |

