# Compile le erlang et le java

# les sources
SRCEFOLDER = erl
SRCE = ring chord logger
SRCJFOLDER = gui
SRCJ = ErlangNode NodeView Noeuds
EXEC = gui.ErlangNode
# Les options de compilations : essentiellement les librairies.
LIBS = "./:/usr/lib/erlang/lib/jinterface-1.5.4/priv/OtpErlang.jar:/usr/lib/erlang/lib/jinterface-1.5.6/priv/OtpErlang.jar"

# La compilation
SOURCEFILESE = $(addprefix $(SRCEFOLDER)/, $(SRCE))
SOURCEFILESJ = $(addprefix $(SRCJFOLDER)/, $(SRCJ:=.java))

main:
	@echo "         ===== linking ====="
	erl -compile $(SOURCEFILESE)
	javac -classpath $(LIBS) $(SOURCEFILESJ)

java:
	java -classpath $(LIBS) $(EXEC)

# Pour le nettoyage complet d'une compilation.
clean:
	rm -f *~ $(SRCJFOLDER)/*.class *.beam *.log

