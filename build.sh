git pull
current=`pwd`
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
sudo chown tinytank -R *
sudo service tinytankweb start
cd $current
echo "Building Data"
sudo tar -zcf MasterTinyTank.tar.gz MasterTinyTank
sudo mv MasterTinyTank.tar.gz /opt/tinytank/.
cd /opt/tinytank
sudo service tinytankdata stop
sudo rm -rf data
sudo tar -zxf MasterTinyTank.tar.gz
sudo mv MasterTinyTank data
cd data
sudo npm install
cd /opt/tinytank
sudo chown tinytank -R *
sudo service tinytankdata start
cd $current
sudo service nginx restart
echo "There you go :)"
