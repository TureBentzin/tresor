{
  description = "Tresor Server - TELNET Gui banking for steamcoin2api";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs";
  };

  outputs = { self, nixpkgs }:
  let
    system = "x86_64-linux";
    pkgs = import nixpkgs { inherit system; };
  in
  {
    packages.${system} = {
        tresor-server = pkgs.callPackage ./package.nix { };
        default = self.packages.${system}.tresor-server;
    };
  };
}
