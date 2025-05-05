{
  description = "TresorGUI Telnet Server";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
    treefmt-nix = {
      url = "github:numtide/treefmt-nix";
      inputs.nixpkgs.follows = "nixpkgs";
    };

  };

  outputs = { self, nixpkgs, treefmt-nix}:
  let
    pkgs = nixpkgs.legacyPackages.x86_64-linux;
    jdk23 = pkgs.jdk23;
    maven = pkgs.maven;
  in {
    packages.x86_64-linux.default = maven.buildMavenPackage rec {
      pname = "tresor";
      version = "1.0-SNAPSHOT";

      src = ./.;

      mvnHash = "";

      nativeBuildInputs = with pkgs; [ makeWrapper ];

      installPhase = ''
        mkdir -p $out/bin $out/share/tresor
        install -Dm644 ./target/Tresor-1.0-SNAPSHOT.jar $out/share/tresor

        makeWrapper ${jdk23}/bin/java $out/bin/tresor \
          --add-flags "-jar $out/share/tresor/Tresor-1.0-SNAPSHOT.jar"
      '';
    };

    devShells.x86_64-linux.default = pkgs.mkShell {
      buildInputs = with pkgs; [jdk23 maven ];
    };
  };
}

