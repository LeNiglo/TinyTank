function web {

  echo "Building Web"
  cd WebTinyTank
  sudo meteor update
  sudo meteor build .
  sudo mv WebTinyTank.tar.gz /opt/tinytank/web/.
  cd /opt/tinytank/web/
  sudo service tinytankweb stop
  sudo rm -rf bundle/
  sudo tar -zxf WebTinyTank.tar.gz
  cd bundle/programs/server/
  sudo npm install
  cd /opt/tinytank
  sudo chown tinytank -R /opt/tinytank
  sudo service tinytankweb start

}

function data {

  echo "Building Data"
  sudo tar -zcf DataTinyTank.tar.gz DataTinyTank
  sudo mv DataTinyTank.tar.gz /opt/tinytank/.
  cd /opt/tinytank
  sudo service tinytankdata stop
  sudo rm -rf data
  sudo tar -zxf DataTinyTank.tar.gz
  sudo mv DataTinyTank data
  cd data
  sudo npm install
  cd /opt/tinytank
  sudo chown tinytank -R /opt/tinytank
  sudo service tinytankdata start

}

git pull
current=`pwd`

if [ $# -eq 0 ]; then
  web
cd $current
  data
else
  $1
fi

cd $current
sudo service nginx restart
sudo rm -f /opt/tinytank/DataTinyTank.tar.gz /opt/tinytank/web/WebTinyTank.tar.gz
echo "There you go :)"
