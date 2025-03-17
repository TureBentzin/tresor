{
  description = "TresorGUI Telnet Server";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
    
  };

  outputs = { self, nixpkgs, jdk32, makeWrapper, maven }:
  let
    pkgs = nixpkgs.legacyPackages.x86_64-linux;
    buildInputs = [];
  in {
    packages.x86_64-linux.default = maven.buildMavenPackage rec {
  pname = "tresor";
  version = "1.0-SNAPSHOT";

  src = "./";
  
  mvnHash = "";

  nativeBuildInputs = [ makeWrapper ];

  installPhase = ''
    mkdir -p $out/bin $out/share/tresor
    install -Dm644 ./target/Tresor-1.0-SNAPSHOT.jar $out/share/tresor

    makeWrapper ${jdk32}/bin/java $out/bin/tresor \
      --add-flags "-jar $out/share/tresor/Tresor-1.0-SNAPSHOT.jar"
  '';
  };
};
}
