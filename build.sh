git pull
$current = `pwd`
cd WebTinyTank
sudo meteor update
sudo meteor build .
sudo mv WebTinyTank.tar.gz /opt/tinytank/web/.
cd /opt/tinytank/web/
sudo service tinytankweb stop
sudo rm -rf bundle/
sudo tar -xvzf WebTinyTank.tar.gz
cd bundle/programs/server/
sudo npm install
cd /opt/tinytank
sudo chown tinytank -R *
sudo service tinytankweb start
cd $current
sudo tar -zcf data.tar.gz MasterTinyTank
sudo mv data.tar.gz /opt/tinytank/.
cd /opt/tinytank
sudo service tinytankdata stop
sudo rm -rf data
sudo tar -zxf data.tar.gz
cd data
sudo npm install
cd /opt/tinytank
sudo chown tinytank -R *
sudo service tinytankdata start
