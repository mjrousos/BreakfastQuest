@REM Build and install puzzle-service
pushd %~dp0puzzle-service
call %M2_HOME%\bin\mvn.cmd clean package install
popd

@REM Build puzzle-service-console
pushd %~dp0puzzle-service-console
call %M2_HOME%\bin\mvn.cmd clean package
popd
