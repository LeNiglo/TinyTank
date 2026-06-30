function web {

  echo "Building Web"
  cd WebTinyTank
  # Nuxt (Nitro) build -> .output/ run by the tinytankweb systemd service:
  #   node /opt/tinytank/web/.output/server/index.mjs
  # The unit must set API_URL, API_AUTH, SENTRY_DSN and PORT in its environment.
  npm ci
  npm run build
  sudo service tinytankweb stop
  sudo rm -rf /opt/tinytank/web/.output /opt/tinytank/web/public
  sudo cp -r .output /opt/tinytank/web/.output
  sudo cp -r public /opt/tinytank/web/public
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
sudo rm -f /opt/tinytank/DataTinyTank.tar.gz
echo "There you go :)"
