Router.configure({
	layoutTemplate: 'layout',
	notFoundTemplate: 'notFound',
	loadingTemplate: 'loading',
	onBeforeAction: function() {
		if (Session.get('authToken')) {
			this.userConnected = true;
		}
		this.next();
	}
});

Router.map(function() {
	this.route('home', {
		path: '/',
		layoutTemplate: 'fullLayout'
	});

	this.route('profile', {
		path: '/profile/:_id?',
		data: function() {
			return this.params;
		}
	});

	this.route('download', {
		layoutTemplate: 'fullLayout'
	});

	this.route('login');
	this.route('register');
	this.route('activation', {
		path: '/active/:_id',
		onRun: function() {
			Meteor.call('active_account', {_idUser: this.params._id}, function(error, result) {
				if (JSON.parse(result.content).res == 1) {
					myAlert("Your account has been activated.", "Well Done,", "success");
				} else {
					myAlert("Please try again.", "Error, ", "danger");
				}
			});
			this.next();
		},
		action: function() {
			this.redirect('profile');
		}
	});


	this.route('servers-list');
	this.route('ladder');
	this.route('about');

	/* Handle 404 */
	this.route('notFound', {
		path: '*'
	});
});
