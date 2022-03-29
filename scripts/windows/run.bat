@echo OFF
start /min rmiregistry.bat
TIMEOUT 6
start /min policy.bat
TIMEOUT 1
start runClient.bat