<!DOCTYPE html>
<html lang="bn">
<head>
    <meta charset="UTF-8">
    <title>SUic Player - GitHub</title>
    <style>
        body {
            font-family: sans-serif;
            background-color: #f9f9f9;
            text-align: center;
            padding-top: 80px;
        }
        a.github-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #24292e;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }
        a.github-link:hover {
            background-color: #444;
        }
    </style>
</head>
<body>
    <h1>SUic Player Webpage</h1>
    <p>এই প্রজেক্টটি ওপেন সোর্স এবং GitHub-এ হোস্ট করা হয়েছে।</p>
    
    <a class="github-link" href="https://github.com/soiadmahedi/SUic-Player" target="_blank">GitHub এ দেখুন</a>

    <?php
        date_default_timezone_set("Asia/Dhaka");
        echo "<p>তারিখ: " . date("d M Y") . "</p>";
    ?>
</body>
</html>
