rmdir /Q /s  "HKAdminCore/build"
rmdir /Q /s  "HKAdminCore/dist"
rmdir /Q /s  "HKCommon/build"
rmdir /Q /s  "HKCommon/dist"

rmdir /Q /s  "HKCore/build"
rmdir /Q /s  "HKCore/dist"

rmdir /Q /s  HKReport/build
rmdir /Q /s  HKReport/dist

rmdir /Q /s  "HKWeb/build"
rmdir /Q /s  "HKWeb/dist"


rmdir /Q /s  "HealthKart/dist/WEB-INF/classes"
DEL /Q      "HealthKart\dist\WEB-INF\lib\hk-core.jar"
DEL /Q      "HealthKart\dist\WEB-INF\lib\hk-admin-core.jar"
DEL /Q      "HealthKart\dist\WEB-INF\lib\hk-common.jar"
DEL /Q      "HealthKart\dist\WEB-INF\lib\hk-report.jar"
pause