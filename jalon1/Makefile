# Compile le erlang et le java

# les sources
SRCEFOLDER = erl
SRCE = ring chord
SRCJFOLDER = gui
SRCJ = ErlangNode NodeView Noeuds
# Les options de compilations : essentiellement les librairies.
LIBS = /usr/lib/erlang/lib/jinterface-1.5.6/priv/OtpErlang.jar

# La compilation
SOURCEFILESE = $(addprefix $(SRCEFOLDER)/, $(SRCE))
SOURCEFILESJ = $(addprefix $(SRCJFOLDER)/, $(SRCJ:=.java))

main:
	@echo "         ===== linking ====="
	erl -compile $(SOURCEFILESE)
	javac -classpath $(LIBS) $(SOURCEFILESJ)


# Pour le nettoyage complet d'une compilation.
clean:
	rm -f *~ *.class *.beam

