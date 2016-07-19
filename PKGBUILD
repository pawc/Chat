# Maintainer: pawc <pswid89@gmail.com>
pkgname=jchat
pkgver=1
pkgrel=1
epoch=
pkgdesc="Chat client written in java"
arch=('x86_64')
url=""
license=('GPLv2')
groups=()
depends=('java-runtime')
makedepends=()
checkdepends=()
optdepends=()
provides=()
conflicts=()
replaces=()
backup=()
options=()
install=
changelog=
source=("http://kritsit.ddns.net/$pkgname-$pkgver.tar.gz")
noextract=()
md5sums=('cde5809514ff0215b764ac5bdedad149')
validpgpkeys=()

prepare() {
	cd "$pkgname-$pkgver"
	#patch -p1 -i "$srcdir/$pkgname-$pkgver.patch"
}

build() {
	cd "$pkgname-$pkgver"
	#./configure --prefix=/usr
	#make
}

check() {
	cd "$pkgname-$pkgver"
	#make -k check
}

package() {
	cd "$pkgname-$pkgver"

	install -d "$pkgdir/usr/share/java/jchat"
	install -m755 "$srcdir/$pkgname-$pkgver/usr/share/java/jchat/ChatClient-1.0-SNAPSHOT-jar-with-dependencies.jar" \
	"$pkgdir/usr/share/java/jchat"

	install -d "$pkgdir/usr/local/bin"
	install -m755 "$srcdir/$pkgname-$pkgver/usr/local/bin/jchat" \
	"$pkgdir/usr/local/bin/"
}
