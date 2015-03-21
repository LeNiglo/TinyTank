Router.configure({
	layoutTemplate: 'layout',
  notFoundTemplate: 'notFound',
  loadingTemplate: 'loading'
});

Router.map(function() {
	this.route('home', {
		path: '/',
		layoutTemplate: 'fullLayout'
	});

	this.route('login');
	this.route('register');

	this.route('servers-list');

	/* Handle 404 */
	this.route('notFound', {
		path: '*'
	});
});
