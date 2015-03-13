<!DOCTYPE html>
<html lang="en">
<head>
	@section('head')
	<meta charset="utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<title>{{{ (isset($page_title) ? $page_title." :: " : "") }}}Tiny Tank</title>

	<!-- CSS -->
	<link href="{{ asset('/css/normalize.css') }}" rel="stylesheet" />
	<link href="{{ asset('/css/app.css') }}" rel="stylesheet" />
	<link href="{{ asset('/css/style.css') }}" rel="stylesheet" />

	<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
	<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
	<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->

	<!-- JS -->
	<script type="text/javascript" src="{{ asset('/js/jquery-2.1.1.min.js') }}"></script>
	<script type="text/javascript" src="{{ asset('/js/bootstrap.min.js') }}"></script>

	@show
</head>
<body>
	<header>
		<nav class="navbar navbar-inverse" style="border-radius: 0;">
			<div class="container-fluid">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#collap">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="/"><b>Tiny Tank:</b> The Game</a>
				</div>

				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<li><a href="/blog">Dev Blog</a></li>
						<li class="dropdown">
							<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">LeNiglo <span class="caret"></span></a>
							<ul class="dropdown-menu" role="menu">
								<li><a href="/profile">My Profile</a></li>
								<li class="divider"></li>
								<li><a href="/logout">Logout</a></li>
							</ul>
						</li>
					</ul>
					<form class="navbar-form navbar-left" role="search">
						<div class="form-group">
							<input type="text" class="form-control" placeholder="Search">
						</div>
						<button type="submit" class="btn btn-primary">Submit</button>
					</form>
					<ul class="nav navbar-nav navbar-right">
						<li><a class="glyphicon glyphicon-folder-open" target="_blank" href="https://github.com/LeNiglo/TinyTank"><i class="alternate github icon"></i> GitHub</a></li>
					</ul>
				</div>
			</div>
		</nav>

	</header>
	@yield('content')
	<footer>{{ Inspiring::quote() }}</footer>
</body>
</html>
