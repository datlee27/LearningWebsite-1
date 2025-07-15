<%@ page import="java.util.*, java.text.SimpleDateFormat, java.util.HashMap, java.util.Map" %>
<%@ page import="your.package.DAO" %> <%-- Change to your actual DAO package --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Profile</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Activity Calendar Styles */
        .activity-calendar {
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.08);
            padding: 20px;
            margin-bottom: 20px;
        }
        .activity-calendar .header {
            font-weight: 600;
            margin-bottom: 18px;
            font-size: 1.1rem;
        }
        .calendar-container {
            margin-bottom: 10px;
        }
        .month-label {
            font-weight: 500;
            margin-bottom: 10px;
        }
        .calendar-grid {
            display: grid;
            grid-template-columns: repeat(10, 1fr);
            gap: 6px;
        }
        .calendar-cell {
            width: 32px;
            height: 32px;
            border-radius: 5px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            position: relative;
            cursor: pointer;
            font-size: 0.95rem;
            transition: background 0.2s;
        }
        .gray { background: #ecf0f1; }
        .light-blue { background: #a2d5f2; }
        .medium-blue { background: #2980b9; color: #fff; }
        .dark-blue { background: #154360; color: #fff; }
        .calendar-cell .day-number {
            z-index: 1;
        }
        .calendar-cell .tooltip {
            visibility: hidden;
            background: #333;
            color: #fff;
            text-align: center;
            border-radius: 4px;
            padding: 4px 8px;
            position: absolute;
            bottom: 110%;
            left: 50%;
            transform: translateX(-50%);
            font-size: 0.85rem;
            z-index: 2;
            opacity: 0.9;
            pointer-events: none;
        }
        .calendar-cell:hover .tooltip {
            visibility: visible;
        }
        .legend {
            margin-top: 15px;
            margin-bottom: 10px;
            font-size: 0.95rem;
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .legend .light-blue, .legend .medium-blue, .legend .dark-blue {
            width: 20px;
            height: 16px;
            display: inline-block;
            border-radius: 3px;
            margin-right: 2px;
        }
        .legend .light-blue { background: #a2d5f2; border: 1px solid #aaa;}
        .legend .medium-blue { background: #2980b9; border: 1px solid #aaa;}
        .legend .dark-blue { background: #154360; border: 1px solid #aaa;}
        .info-text {
            font-size: 0.88rem;
            color: #888;
            margin-top: 3px;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <div class="row">
        <!-- Left Column -->
        <div class="col-lg-6 mb-4">
            <!-- Profile Edit Card -->
            <div class="card mb-4">
                <div class="card-header">
                    Edit Profile
                </div>
                <div class="card-body">
                    <form>
                        <div class="mb-3">
                            <label for="username" class="form-label">Username</label>
                            <input type="text" class="form-control" id="username" name="username"
                                   value="<%= session.getAttribute("username") != null ? session.getAttribute("username") : "" %>" readonly>
                        </div>
                        <div class="mb-3">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<%= session.getAttribute("email") != null ? session.getAttribute("email") : "" %>">
                        </div>
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </form>
                </div>
            </div>
            <!-- Password Edit Card -->
            <div class="card">
                <div class="card-header">
                    Change Password
                </div>
                <div class="card-body">
                    <form>
                        <div class="mb-3">
                            <label for="currentPassword" class="form-label">Current Password</label>
                            <input type="password" class="form-control" id="currentPassword" name="currentPassword">
                        </div>
                        <div class="mb-3">
                            <label for="newPassword" class="form-label">New Password</label>
                            <input type="password" class="form-control" id="newPassword" name="newPassword">
                        </div>
                        <button type="submit" class="btn btn-secondary">Update Password</button>
                    </form>
                </div>
            </div>
        </div>
        <!-- Right Column: Activity Calendar -->
        <div class="col-lg-6">
            <div class="card activity-calendar">
                <div class="header">
                    <span>
                        <%= session.getAttribute("username") != null
                            ? "Activity for " + session.getAttribute("username")
                            : "21 contributions" %> in the last 30 days
                    </span>
                </div>
                <div class="calendar-container">
                    <div>
                        <%
                            Calendar now = Calendar.getInstance();
                            now.setTime(new java.util.Date());
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
                            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                            String currentMonth = monthFormat.format(now.getTime());
                            String currentYear = yearFormat.format(now.getTime());
                        %>
                        <div class="month-label"><%= currentMonth %></div>
                        <div class="calendar-grid" id="calendarGrid">
                            <%
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String today = sdf.format(now.getTime());

                                // Lấy thời gian đăng nhập từ session
                                Long loginTime = (Long) session.getAttribute("loginTime");
                                Integer historicalMinutesToday = (Integer) session.getAttribute("historicalMinutesToday");
                                if (historicalMinutesToday == null) historicalMinutesToday = 0;

                                if (loginTime == null && session.getAttribute("username") != null) {
                                    loginTime = System.currentTimeMillis();
                                    session.setAttribute("loginTime", loginTime);
                                    new DAO().logLogin((String) session.getAttribute("username"));
                                }
                                long currentTime = System.currentTimeMillis();
                                long sessionDuration = (loginTime != null) ? (currentTime - loginTime) / (1000 * 60) : 0;

                                // Lấy dữ liệu từ DB
                                DAO dao = new DAO();
                                String username = (String) session.getAttribute("username");
                                Map<String, Integer> onlineTimes = new HashMap<>();
                                if (username != null) {
                                    onlineTimes = dao.getUserActivity(username);
                                    // Cập nhật thời gian online cho ngày hiện tại nếu đang online
                                    if (loginTime != null) {
                                        onlineTimes.put(today, historicalMinutesToday + (int) sessionDuration);
                                    }
                                }

                                Calendar cal = (Calendar) now.clone();
                                cal.add(Calendar.DAY_OF_MONTH, -29); // Bắt đầu từ 30 ngày trước
                                while (!cal.after(now)) {
                                    String date = sdf.format(cal.getTime());
                                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                                    int minutes = onlineTimes.getOrDefault(date, 0);
                                    String colorClass = "gray";
                                    if (minutes > 180) colorClass = "dark-blue";
                                    else if (minutes > 60) colorClass = "medium-blue";
                                    else if (minutes > 0) colorClass = "light-blue";
                            %>
                            <div class="calendar-cell <%=colorClass%>" data-date="<%=date%>" data-minutes="<%=minutes%>">
                                <span class="day-number"><%= dayOfMonth %></span>
                                <span class="tooltip"><%= minutes > 0 ? minutes + " minutes" : "No activity" %></span>
                            </div>
                            <%
                                    cal.add(Calendar.DAY_OF_YEAR, 1);
                                }
                            %>
                        </div>
                        <div style="margin-top: 15px; color: #bdc3c7;"><%= currentMonth %> <%= currentYear %></div>
                    </div>
                </div>
                <div class="legend">
                    Less
                    <span class="light-blue"></span>
                    <span class="medium-blue"></span>
                    <span class="dark-blue"></span>
                    More
                </div>
                <div class="info-text">Learn how we count contributions</div>
                <div class="info-text">Contribution activity</div>
            </div>
        </div>
    </div>
</div>
<!-- Bootstrap JS (optional, for some features) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.addEventListener('DOMContentLoaded', () => {
        const cells = document.querySelectorAll('.calendar-cell');
        cells.forEach(cell => {
            cell.addEventListener('mouseover', () => {
                const tooltip = cell.querySelector('.tooltip');
                tooltip.style.visibility = 'visible';
            });
            cell.addEventListener('mouseout', () => {
                const tooltip = cell.querySelector('.tooltip');
                tooltip.style.visibility = 'hidden';
            });
        });

        // Cập nhật thời gian thực cho ngày hiện tại
        const now = new Date();
        const today = now.toISOString().split('T')[0];
        const todayCell = document.querySelector(`[data-date="${today}"]`);
        const historicalMinutes = <%= historicalMinutesToday %>;
        if (todayCell) {
            setInterval(() => {
                const loginTime = <%= loginTime != null ? loginTime : 0 %>;
                if (loginTime > 0) {
                    const currentTime = new Date().getTime();
                    const realTimeDuration = Math.floor((currentTime - loginTime) / 1000 / 60);
                    const totalMinutes = historicalMinutes + realTimeDuration;
                    todayCell.dataset.minutes = totalMinutes;
                    const colorClass = totalMinutes > 180 ? 'dark-blue' :
                                      totalMinutes > 60 ? 'medium-blue' :
                                      totalMinutes > 0 ? 'light-blue' : 'gray';
                    todayCell.className = `calendar-cell ${colorClass}`;
                    todayCell.querySelector('.tooltip').textContent = totalMinutes > 0 ? totalMinutes + " minutes" : "No activity";
                }
            }, 60000); // Cập nhật mỗi phút
        }
    });
</script>
</body>
</html>
