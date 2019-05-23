#############################################
#											#
# NIS Makefile								#
#											#
# @author Matthew Poulter (PLTMAT001)		#
# @version 1.1, 23 May 2019					#
#											#
#############################################

# Variables
JAVAC := /usr/bin/javac
JAVA := /usr/bin/java
JAVADOC := /usr/bin/javadoc
LIB := lib:lib/*:lib/bcprov-jdk15on-1.61.jar
SRC := src
BIN := bin
TESTBIN := testbin
KEYS := keys
DOC := docs
TEST := test
SRC_F := $(wildcard src/ClientServer/*.java)
TEST_F := $(wildcard test/ClientServerTests/*.java)

LIST := $(SRC_F:$(SRC)/%.java=$(BIN)/%.class)
TESTLIST := $(TEST_F:$(TEST)/%.java=$(TESTBIN)/%.class)

.PHONY: all test clean keys server client

# Default - make
all: setup $(LIST) $(TESTLIST) keys complete

# Clean
clean:
	@rm -R $(BIN) $(TESTBIN) $(DOC) $(KEYS)

# Setup
setup:
	@echo Compiling...
	@mkdir -p $(BIN)/ClientServer $(TESTBIN)/ClientServerTests $(DOC) $(KEYS)

# Build
$(BIN)/%.class: $(SRC)/%.java | $(BIN)
	@$(JAVAC) -cp $(LIB):$(BIN):$(SRC) -sourcepath $(SRC) -g -d $| $<

# Build Tests
$(TESTBIN)/%.class: $(TEST)/%.java | $(TESTBIN)
	@$(JAVAC) -cp $(LIB):$(BIN):$(TESTBIN):$(SRC):$(TEST) -sourcepath $(TEST) -g -d $| $<

# Complete
complete:
	@echo Complete!
	
# Docs	
docs:
	@echo Generating docs...
	@$(JAVADOC) -quiet -cp $(LIB):$(BIN) -sourcepath $(SRC) -d $(DOC) $(SRC_F)
	@echo Complete!

# Test
test:
	@echo Running tests
	@echo Please be patient...
	@$(JAVA) -jar lib/junit-platform-console-standalone-1.4.0.jar -cp $(LIB):$(BIN):$(TESTBIN) -p ClientServerTests
	@echo Complete!

# Run server
server:
	@$(JAVA) -cp $(LIB):$(BIN) ClientServer/Server

# Run client
client:
	@$(JAVA) -cp $(LIB):$(BIN) ClientServer/Client

# Generate new keys
keys:
	@echo Generating new public and private keys
	@$(JAVA) -cp $(LIB):$(BIN) ClientServer/RSA
