{ lib, makeWrapper, maven , jdk23}:

maven.buildMavenPackage rec {
  pname = "tresor";
  version = "1.0-SNAPSHOT";

  src = ./.;
  #sourceRoot = "tresor-server/Tresor";
  subdir = "tresor-server/Tresor";

  mvnHash = "";

  nativeBuildInputs = [ makeWrapper ];

  mvnJdk = jdk23;
  mvnFetchExtraArgs = { preBuild = ''
        ls -lsa
        ls .git
      '';
  };

  installPhase = ''
    mkdir -p $out/bin $out/share/tresor
    install -Dm644 target/tresor.jar $out/share/tresor

    makeWrapper ${jdk23}/bin/java $out/bin/tresor \
      --add-flags "-jar $out/share/tresor/tresor.jar"
  '';
    meta = {
    description = "Tresor Server - TELNET Gui banking for steamcoin2api";
    homepage = "https://github.com/TureBentzin/tresor";
  };

}