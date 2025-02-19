{
  description = "Tresor Server - TELNET Gui banking for steamcoin2api";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs";
  };

  outputs = { self, nixpkgs }:
  let
    system = "x86_64-linux";
    pkgs = nixpkgs.${system};
  in {
    pkgs.tresor-server = pkgs.stdenv.mkDerivation {
      pname = "tresor-server";
      version = "1.0-SNAPSHOT";

      src = ./tresor-server/Tresor; # mvn project

      nativeBuildInputs = [ pkgs.maven pkgs.jdk23 ];

      buildPhase = ''
        mvn package
      '';

      # https://gist.github.com/tech-otaku/e96fae365158a3802e8109508d2adf7f
      installPhase = ''
        mkdir -p $out/bin
        cp target/Tresor-*.jar $out/lib/tresor.jar

        cat > $out/bin/tresor-server <<EOF
        #!/bin/sh
        exec ${pkgs.jdk23}/bin/java -jar $out/lib/tresor.jar "\$@"
        EOF
        chmod +x $out/bin/tresor-server
      '';

      meta = {
        description = "Tresor Server - TELNET Gui banking for steamcoin2api";
        homepage = "https://github.com/TureBentzin/tresor";
      };
    };

    #warning: flake output attribute 'defaultPackage' is deprecated; use 'packages.<system>.default' instead
    packages.${system}.tresor-server = pkgs.tresor-server;
     # defaultPackage.${system} = self.packages.${system}.tresor-server;
  };
}
