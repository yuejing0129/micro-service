@echo off & setlocal enabledelayedexpansion

title ms-log

% 启动 %
echo Starting ...

set project.dir=${user.dir}/../../
java -Xms256m -Xmx256m -XX:MaxPermSize=64M -Dproject.dir=%project.dir% -jar ..\..\ms-cloud-log-1.0.0.jar

:end
pause