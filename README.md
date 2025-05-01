# O(n) SSG-DEAL: 이벤트성 특가 프로모션 이커머스 플랫폼
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

</br>
</br>

### 🌱 유저 서비스의 JWT/Passport 도입
> 유저 서비스에서는 분산된 MSA 환경에서도 일관된 인증 체계를 갖추고자 JWT 토큰을 이용하여 게이트웨이에서 검증을 진행하였습니다.
> 그러나 JWT 토큰으로 인증을 진행한 이후로, 타 서비스에서 유저의 상세 정보와 인증 정보를 얻으려면 지속적으로 유저 서비스와 인증 서비스를 호출해야 했고, 이 단점을 줄이고자 게이트웨이에서 JWT 토큰으로 인증을 마치면 내부 통신용 인증 객체인 패스포트 토큰을 발급하였습니다.

</br>
</br>

### 🌿 프로모션 Redis/Lua Script 도입
> 프로모션 서비스에서는 특판 서비스 특성 상, 다수의 사용자가 동시에 주문을 요청하는 상황이 발생했습니다. 이때, 단순한 서버 간 동기화로는 분산된 MSA 환경에서 일관성을 보장할 수 없었고, 주문 시 프로모션 서비스에서 데이터 변동이 자주 발생하는 상품 도메인에 대해 Redis 캐싱과 Lua Script를 적용해 속도 개선과 함께 동시성 이슈를 해결하고자 했습니다.
> Redis의 인메모리 DB 특성인 빠른 접근 속도를 이용해 프로모션이 진행중인 상품 정보를 올려두어 처리 속도를 높였습니다.
> Lua Script를 이용하여 단일 레디스 환경에서 단일 스레드 특성과 atmic 연산 특성을 이용해 Redis에서 락 획득, 비즈니스 로직, 락 해제를 하나의 원자적 작업으로 실행, 처리 중 중단이나 경합을 막았습니다.

</br>
</br>

### ☘️ 주문 서비스의 Kafka 도입
> 주문 서비스에서는 주문-결제 실패 이후 재고 수량 증가(보상 트랜잭션), 주문-결제 성공 시나리오 이후 알림 발송 등 후속 처리가 필요했습니다. 초기에는 주문 트랜잭션 내에서 모든 작업을 동기적으로 처리했으나, 만약 재고 상승 및 알림 발송 등 주문과 연관 없는 비즈니스 로직의 장애가 주문까지 전파되었습니다.
> 이를 해결하기 위해 Kafka를 도입하여 이벤트 기반 아키텍처로 전환했습니다. 주문 완료 시점에 주문 완료 이벤트를 발행하고, 재고 서비스와 알림 서비스는 각각 이벤트를 구독하여 후속 처리를 수행하도록 분리하였습니다.

</br>
</br>

### 🍀 배포 환경 ECS 이유
> 서비스의 안정적인 배포와 각 컨테이너로 구동되는 서비스의 관리 유용성을 위해 AWS ECS를 도입했고, 클러스터는 EC2, 태스크 인스턴스는 Fargate로 운영했습니다. 이를 통해 서버 리소스를 유연하게 관리하면서도, 필요한 만큼만 인스턴스를 사용할 수 있어 비용을 절감할 수 있었습니다. 또한 CI/CD는 모노레포 구조로 관리하면서, 각 서비스별 패키지 루트 단위로 ECR 레포지토리를 분리해 빌드와 배포를 진행하여, 빠른 배포와 서비스 간 영향 최소화를 동시에 달성할 수 있었습니다.

</br>
</br>

### 🌲 운영 로그 모니터링 CLOUD WATCH 도입
> 운영 환경의 서비스 로그를 효과적으로 모니터링하기 위해 AWS CloudWatch를 도입했습니다. 초기에는 ELK(Stack)를 고려했지만, 인프라 비용을 월 20만 원 이내로 한정된 자원으로 생각하였습니다. 이에 따라 비용 부담을 줄이기 위해 CloudWatch를 선택했습니다. 또한 ECS와 CloudWatch는 기본적으로 쉽게 연동할 수 있어 별도의 복잡한 설정 없이 빠르게 로그 수집 및 모니터링 체계를 구축할 수 있었고, 이를 통해 장애 대응과 서비스 안정성 확보에 기여했습니다.

</br>
</br>

## 🚨 Trouble Shooting
### Kafka 요청 시 JPA AuditingConfig 에러 [LINK](https://teamsparta.notion.site/Kafka-JPA-AuditingConfig-1e42dc3ef5148046a60bf76bd4b966b4)
### ECS 배포 중 생긴 태스크 실행 문제 [LINK](https://teamsparta.notion.site/ECS-1e42dc3ef514801ea641e3d7103fdf3a)
### ECS 내부 네트워크 통신 문제 [LINK](https://teamsparta.notion.site/ECS-1e42dc3ef51480caa085c8f1658a4339)

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

